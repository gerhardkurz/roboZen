package edu.kit.robocup.tree;


import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.Dash;
import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.game.Turn;
import edu.kit.robocup.game.controller.IPlayerController;
import edu.kit.robocup.game.controller.Team;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.interf.game.IPlayer;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.interf.mdp.IState;
import edu.kit.robocup.mdp.PlayerActionSet;
import edu.kit.robocup.mdp.PlayerActionSetFactory;
import edu.kit.robocup.mdp.transition.ITransition;
import edu.kit.robocup.mdp.transition.TransitionDet;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.util.*;
import java.time.Instant;

import static edu.kit.robocup.game.Action.DASH;
import static edu.kit.robocup.game.Action.KICK;

public class TreePolicy implements IPolicy {
    private static Logger logger = Logger.getLogger(TreePolicy.class.getName());
    private ITransition transition;
    private IPruner pruner;
    private IReward reward;
    private List<PlayerActionSet> actions;
    private Duration duration;

    public TreePolicy(Team enemyTeam) {
        this(new TransitionDet(2 , 4, -1, enemyTeam), new BallPositionPruner(), new TreeReward(), new PlayerActionSetFactory().getActionPermutations(2, 6, 1, 3), Duration.ofMillis(1000 -100));
    }

    public TreePolicy() {
        this(null);
    }

    public TreePolicy(ITransition transition, IPruner pruner, IReward reward, List<PlayerActionSet> actions, Duration duration) {
        this.transition = transition;
        this.pruner = pruner;
        this.reward = reward;
        this.actions = actions;
        logger.info("Permutations: " + actions.size());
        this.duration = duration;
    }

    private class BfsNode {
        private IState start;
        private IState end;
        private PlayerActionSet actions;
        private double rew;
        private List<PlayerActionSet> before;

        private BfsNode(IState start, IState end, PlayerActionSet actions, double rew, List<PlayerActionSet> before) {
            this.start = start;
            this.end = end;
            this.actions = actions;
            this.rew = rew;
            this.before = new ArrayList<>();
            for (PlayerActionSet p : before) {
                this.before.add(p);
            }
        }

        public void addAction(PlayerActionSet p) {
            before.add(p);
        }
    }

    private PlayerActionSet bfs(IState state, PitchSide pitchSide) {
        Instant end = Instant.now().plus(duration);
        List<BfsNode> currNodes = new LinkedList<>();
        List<BfsNode> nextNodes = new LinkedList<>();
        currNodes.add(new BfsNode(state, state, null, -Double.MAX_VALUE, new ArrayList<PlayerActionSet>()));
        Iterator<BfsNode> currIterator = currNodes.iterator();
        int depth = 0;
        int prune = 0;
        int maxdepth = 0;
        double maxReward = -Double.MAX_VALUE;
        PlayerActionSet bestPlayerActionSet = null;
        while(Instant.now().isBefore(end) && (currIterator.hasNext() || !nextNodes.isEmpty())) {
            if (!currIterator.hasNext()) {
                // sort nodes in decending order of reward value
                Collections.sort(nextNodes, new Comparator<BfsNode>() {
                    @Override
                    public int compare(BfsNode o1, BfsNode o2) {
                        if (o1.rew == o2.rew) {
                            return 0;
                        } else if (o1.rew > o2.rew) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });
                if (nextNodes.size() > 100) {
                    currNodes = nextNodes.subList(0, 100);
                } else {
                    currNodes = nextNodes;
                }
                maxReward = nextNodes.get(0).rew;
                bestPlayerActionSet = nextNodes.get(0).actions;
                maxdepth = depth;
                nextNodes = new LinkedList<>();
                currIterator = currNodes.iterator();
                //logger.info("Depth: " + depth + " currNodes: " + currNodes.size() + " pruned " + prune);
                depth++;
                prune = 0;
            }
            BfsNode node = currIterator.next();
            boolean firstPlayerKickable = false;
            if (node.end.getPlayers(pitchSide).get(0).getDistance(node.end.getBall()) <= Constants.KICKABLE_MARGIN) {
                firstPlayerKickable = true;
            }
            /*if (depth == 0) {
                logger.info("Player 1 is just " + node.end.getPlayers(pitchSide).get(0).getDistance(node.end.getBall()) + " far away of ball");
                logger.info(state);
            }*/
            boolean secondPlayerKickable = false;
            if (node.end.getPlayers(pitchSide).get(1).getDistance(node.end.getBall()) <= Constants.KICKABLE_MARGIN) {
                secondPlayerKickable = true;
            }
            /*if (depth == 0) {
                    logger.info("Player 2 is just " + node.end.getPlayers(pitchSide).get(1).getDistance(node.end.getBall()) + " far away of ball");
                    logger.info(state);
            }*/
            PlayerActionSetFactory factory = new PlayerActionSetFactory();
            List<PlayerActionSet> playerActionSets = factory.getActionPermutationsWithAngles(2, 3, 1, 3, state, pitchSide);
            //if (!pruner.prune(node.start, node.end, pitchSide)) {
                for (PlayerActionSet playerActionSet : playerActionSets) {
                    if ((firstPlayerKickable) || (!(playerActionSet.getActions().get(0).getActionType() == KICK))) {
                        if ((secondPlayerKickable) || (!(playerActionSet.getActions().get(1).getActionType() == KICK))) {

                            IState next;
                            if (transition.hasEnemyTeam()) {
                                next = transition.getNewStateSampleWithEnemyPolicy((State) node.end, playerActionSet, pitchSide);
                            } else {
                                next = transition.getNewStateSample((State) node.end, playerActionSet, pitchSide);
                            }

                            PlayerActionSet pas = node.actions == null ? playerActionSet : node.actions;
                            double newReward = reward.getReward(next, pitchSide)* Math.pow(0.9, node.before.size());
                            if (node.before.size() != 0) {
                                newReward += node.rew;
                            }
                            BfsNode n = new BfsNode(node.end, next, pas, newReward, node.before);
                            n.addAction(playerActionSet);
                            nextNodes.add(n);
                            /*double possMaxReward = 0;
                            if (n.before.size() >= 1) {
                                possMaxReward = newReward/(n.before.size() * 1.0);
                            }
                            if (possMaxReward > maxReward) {
                                maxReward = newReward;
                                bestPlayerActionSet = pas;
                                maxdepth = depth;
                            }*/
                        }
                    }
                }
            /*} else {
                prune++;
            }*/
        }
        logger.info("Bfs depth: " + depth);
        logger.info("Best reward: " + maxReward);
        logger.info("Best action: " + bestPlayerActionSet);
        logger.info("Maxdepth: " + maxdepth);
        return bestPlayerActionSet;
    }


    @Override
    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> playerControllers, PitchSide pitchSide) {
        logger.info(state);
        PlayerActionSet playerActionSet = bfs(state, pitchSide);
        //logger.info(transition.getNewStateSample((State) state, playerActionSet, pitchSide));
        Map<IPlayerController, IAction> actions = new HashMap<>();
        Iterator<? extends IPlayerController> playerControllerIterator = playerControllers.iterator();
        Iterator<PlayerAction> playerActionIterator = playerActionSet.getActions().iterator();
        while(playerControllerIterator.hasNext() && playerActionIterator.hasNext()) {
            actions.put(playerControllerIterator.next(), playerActionIterator.next().getAction());
        }
        return actions;
    }
}














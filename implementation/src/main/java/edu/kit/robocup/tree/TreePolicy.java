package edu.kit.robocup.tree;


import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.game.controller.IPlayerController;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IAction;
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

import static edu.kit.robocup.game.Action.KICK;

public class TreePolicy implements IPolicy {
    private static Logger logger = Logger.getLogger(TreePolicy.class.getName());
    private ITransition transition;
    private IPruner pruner;
    private IReward reward;
    private List<PlayerActionSet> actions;
    private Duration duration;

    public TreePolicy() {
        this(new TransitionDet(2 , 4, -1), new BallPositionPruner(), new TreeReward(), new PlayerActionSetFactory().getActionPermutations(2, 10, 1, 3), Duration.ofMillis(100));
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

        private BfsNode(IState start, IState end, PlayerActionSet actions) {
            this.start = start;
            this.end = end;
            this.actions = actions;
        }
    }

    private PlayerActionSet bfs(IState state, PitchSide pitchSide) {
        Instant end = Instant.now().plus(duration);
        List<BfsNode> currNodes = new LinkedList<>();
        List<BfsNode> nextNodes = new LinkedList<>();
        currNodes.add(new BfsNode(state, state, null));
        Iterator<BfsNode> currIterator = currNodes.iterator();
        int depth = 0;
        int prune = 0;
        double maxReward = -Double.MAX_VALUE;
        PlayerActionSet bestPlayerActionSet = null;
        while(Instant.now().isBefore(end) && (currIterator.hasNext() || !nextNodes.isEmpty())) {
            if (!currIterator.hasNext()) {
                currNodes = nextNodes;
                nextNodes = new LinkedList<>();
                currIterator = currNodes.iterator();
                depth++;
                logger.info("Depth: " + depth + " currNodes: " + currNodes.size() + " pruned " + prune);
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
            if (!pruner.prune(node.start, node.end, pitchSide)) {
                for (PlayerActionSet playerActionSet: actions) {
                    if ((firstPlayerKickable) || (!(playerActionSet.getActions().get(0).getActionType() == KICK))) {
                        if ((secondPlayerKickable) || (!(playerActionSet.getActions().get(1).getActionType() == KICK))) {
                            IState next = transition.getNewStateSample((State) node.end, playerActionSet, pitchSide);
                            PlayerActionSet pas = node.actions == null ? playerActionSet : node.actions;
                            nextNodes.add(new BfsNode(node.start, next, pas));
                            double newReward = reward.getReward(next, pitchSide);
                            if (newReward > maxReward) {
                                maxReward = newReward;
                                bestPlayerActionSet = pas;
                            }
                        }
                    }
                }
            } else {
                prune++;
            }
        }
        logger.info("Bfs depth: " + depth);
        logger.info("currNodes " + nextNodes.size() + " pruned " + prune);
        return bestPlayerActionSet;
    }


    @Override
    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> playerControllers, PitchSide pitchSide) {
        PlayerActionSet playerActionSet = bfs(state, pitchSide);
        //logger.info(state);
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














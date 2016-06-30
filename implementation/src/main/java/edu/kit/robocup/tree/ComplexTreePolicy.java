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
import edu.kit.robocup.mdp.transition.ITransition;
import edu.kit.robocup.mdp.transition.TransitionDet;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static edu.kit.robocup.game.Action.KICK;


public class ComplexTreePolicy implements IPolicy {
    private static Logger logger = Logger.getLogger(ComplexTreePolicy.class.getName());
    private ITransition transition;
    private IReward reward;
    private Duration duration;
    private IComplexAction[] playerActionSets = {new ComplexKick(), new ComplexRun(), new ComplexPass()};

    public ComplexTreePolicy() {
        this(new TransitionDet(2, 4, -1), new TreeReward(), Duration.ofMillis(100));
    }

    public ComplexTreePolicy(ITransition transition, IReward reward, Duration duration) {
        this.transition = transition;
        this.reward = reward;
        this.duration = duration;
    }

    private class BfsNode {
        private IState start;
        private IState end;
        private IComplexAction actions;

        private BfsNode(IState start, IState end, IComplexAction actions) {
            this.start = start;
            this.end = end;
            this.actions = actions;
        }
    }

    private PlayerActionSet bfs(IState state, PitchSide pitchSide) {
        Instant end = Instant.now().plus(duration);
        List<ComplexTreePolicy.BfsNode> currNodes = new LinkedList<>();
        List<ComplexTreePolicy.BfsNode> nextNodes = new LinkedList<>();
        currNodes.add(new ComplexTreePolicy.BfsNode(state, state, null));
        Iterator<ComplexTreePolicy.BfsNode> currIterator = currNodes.iterator();
        int depth = 0;
        double maxReward = -Double.MAX_VALUE;
        IComplexAction bestComplexAction = null;
        while (Instant.now().isBefore(end) && (currIterator.hasNext() || !nextNodes.isEmpty())) {
            if (!currIterator.hasNext()) {
                currNodes = nextNodes;
                nextNodes = new LinkedList<>();
                currIterator = currNodes.iterator();
                depth++;
                logger.info("Depth: " + depth + " currNodes: " + currNodes.size());
            }
            ComplexTreePolicy.BfsNode node = currIterator.next();

            for (IComplexAction complexAction : playerActionSets) {
                if (complexAction.isPossible(state, pitchSide)) {
                    IState next = node.end;
                    for (PlayerActionSet playerActionSet : complexAction.getPlayerActionSets(state, pitchSide)) {
                        next = transition.getNewStateSample((State) next, playerActionSet, pitchSide);
                    }
                    IComplexAction newComplexAction = node.actions == null ? complexAction : node.actions;
                    double newReward = reward.getReward(next, pitchSide);
                    if (newReward > maxReward) {
                        maxReward = newReward;
                        bestComplexAction = newComplexAction;
                    }
                    nextNodes.add(new ComplexTreePolicy.BfsNode(node.start, next, newComplexAction));
                }


            }
        }
        logger.info("Bfs depth: " + depth);
        logger.info("currNodes " + nextNodes.size());
        return bestComplexAction.getPlayerActionSets(state, pitchSide).get(0);
    }


    @Override
    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> playerControllers, PitchSide pitchSide) {
        PlayerActionSet playerActionSet = bfs(state, pitchSide);
        Map<IPlayerController, IAction> actions = new HashMap<>();
        Iterator<? extends IPlayerController> playerControllerIterator = playerControllers.iterator();
        Iterator<PlayerAction> playerActionIterator = playerActionSet.getActions().iterator();
        while (playerControllerIterator.hasNext() && playerActionIterator.hasNext()) {
            actions.put(playerControllerIterator.next(), playerActionIterator.next().getAction());
        }
        return actions;
    }
}

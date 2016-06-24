package edu.kit.robocup.tree;


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

import java.time.Duration;
import java.util.*;
import java.time.Instant;

public class TreePolicy implements IPolicy {
    private ITransition transition;
    private IPruner pruner;
    private IReward reward;
    private List<PlayerActionSet> actions;
    private Duration duration;

    public TreePolicy() {
        this(new TransitionDet(-1 , -1, -1), null, null, new PlayerActionSetFactory().getReducedActions(), Duration.ofMillis(100));
    }

    public TreePolicy(ITransition transition, IPruner pruner, IReward reward, List<PlayerActionSet> actions, Duration duration) {
        this.transition = transition;
        this.pruner = pruner;
        this.reward = reward;
        this.actions = actions;
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
        while(Instant.now().isBefore(end) && (currIterator.hasNext() || !nextNodes.isEmpty())) {
            if (!currIterator.hasNext()) {
                currNodes = nextNodes;
                nextNodes = new LinkedList<>();
                currIterator = currNodes.iterator();
            }
            BfsNode node = currIterator.next();
            if (!pruner.prune(node.start, node.end)) {
                for (PlayerActionSet playerActionSet: actions) {
                    IState next = transition.getNewStateSample((State) node.end, playerActionSet, pitchSide);
                    nextNodes.add(new BfsNode(node.start, next, node.actions == null ? playerActionSet : node.actions));
                }
            }
        }
        return getBestActions(currNodes);
    }

    private PlayerActionSet getBestActions(List<BfsNode> nodes) {
        float maxReward = Float.MIN_VALUE;
        PlayerActionSet bestActions = null;

        int currentReward = 0;
        int count = 0;
        PlayerActionSet currActions = nodes.get(0).actions;
        for (BfsNode node: nodes) {
            if (node.actions != currActions) {
                float newReward = (float) currentReward / (float) count;
                if (newReward > maxReward) {
                    maxReward = newReward;
                    bestActions = currActions;
                }
                currActions = node.actions;
                currentReward = 0;
                count = 0;
            }
            currentReward += reward.reward(node.start, node.end);
            count++;
        }

        if ((float) currentReward / (float) count > maxReward) {
            bestActions = currActions;
        }

        return bestActions;
    }


    @Override
    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> playerControllers, PitchSide pitchSide) {
        PlayerActionSet playerActionSet = bfs(state, pitchSide);
        Map<IPlayerController, IAction> actions = new HashMap<>();
        Iterator<? extends IPlayerController> playerControllerIterator = playerControllers.iterator();
        Iterator<PlayerAction> playerActionIterator = playerActionSet.getActions().iterator();
        while(playerControllerIterator.hasNext() && playerActionIterator.hasNext()) {
            actions.put(playerControllerIterator.next(), playerActionIterator.next().getAction());
        }
        return actions;
    }
}














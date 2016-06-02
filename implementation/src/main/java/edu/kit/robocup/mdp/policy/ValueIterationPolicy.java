package edu.kit.robocup.mdp.policy;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.Dash;
import edu.kit.robocup.game.Kick;
import edu.kit.robocup.game.Turn;
import edu.kit.robocup.game.controller.IPlayerController;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.interf.mdp.IActionSet;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.interf.mdp.IState;
import edu.kit.robocup.mdp.ActionSet;
import edu.kit.robocup.mdp.Reward;
import edu.kit.robocup.mdp.transition.Transitions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ValueIterationPolicy implements IPolicy {

    private DoubleMatrix1D theta;
    private Reward r;
    private Transitions t;

    public ValueIterationPolicy(DoubleMatrix1D theta, Reward r, Transitions t) {
        this.theta = theta;
        this.r = r;
        this.t = t;
    }

    @Override
    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> playerControllers, PitchSide pitchSide) {
        // out of all actions that exist you should choose the action, which maximizes the immediate reward + theta*nextState
        Map<IPlayerController, IAction> action = new HashMap<>();
        List<IAction> reducedActions = new ArrayList<>();
        reducedActions.add(new Kick(30,0));
        reducedActions.add(new Kick(30,25));
        reducedActions.add(new Kick(30,-25));
        reducedActions.add(new Turn(1));
        reducedActions.add(new Turn(10));
        reducedActions.add(new Turn(50));
        reducedActions.add(new Turn(-1));
        reducedActions.add(new Turn(-10));
        reducedActions.add(new Turn(-50));
        reducedActions.add(new Dash(40));
        List<IActionSet> permutations = new ArrayList<>();
        for (int i = 0; i < reducedActions.size(); i++) {
            for (int j = 0; j < reducedActions.size(); j++) {
                List<IAction> t = new ArrayList<>();
                t.add(reducedActions.get(i));
                t.add(reducedActions.get(j));
                permutations.add(new ActionSet(t));
            }
        }
        int maxActionPermutation = 0;
        int maxValue = 0;
        int K = 10;
        for (int i = 0; i < permutations.size(); i++) {
            int value = 0;
            for (int k = 0; k < K; k++) {
                State s = t.getNewStateSample((State) state, permutations.get(i), r.getPitchSide());
                DoubleFactory1D h = DoubleFactory1D.dense;
                DoubleMatrix1D next = h.make(s.getArray());
                value += r.calculateReward((State) state, permutations.get(i), s);
                value += theta.zDotProduct(next);
            }
            if (maxValue < value) {
                maxValue = value;
                maxActionPermutation = i;
            }
        }
        action.put(playerControllers.get(0), reducedActions.get(maxActionPermutation/10));
        action.put(playerControllers.get(1), reducedActions.get(maxActionPermutation % 10));
        return action;
    }
}

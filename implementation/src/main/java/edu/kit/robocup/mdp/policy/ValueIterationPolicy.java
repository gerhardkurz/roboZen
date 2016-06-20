package edu.kit.robocup.mdp.policy;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.Turn;
import edu.kit.robocup.game.controller.IPlayerController;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.interf.mdp.IReward;
import edu.kit.robocup.interf.mdp.IState;
import edu.kit.robocup.mdp.PlayerActionSet;
import edu.kit.robocup.mdp.PlayerActionSetFactory;
import edu.kit.robocup.mdp.Value;
import edu.kit.robocup.mdp.transition.ITransition;
import edu.kit.robocup.mdp.transition.Transition;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.kit.robocup.game.Action.DASH;
import static edu.kit.robocup.game.Action.KICK;


public class ValueIterationPolicy implements IPolicy {

    static Logger logger = Logger.getLogger(ValueIterationPolicy.class.getName());

    private Value v;
    private IReward r;
    private ITransition t;

    public ValueIterationPolicy(Value v, IReward r, ITransition t) {
        this.v = v;
        this.r = r;
        this.t = t;
    }

    @Override
    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> playerControllers, PitchSide pitchSide) {
        Map<IPlayerController, IAction> action = new HashMap<>();
        if (((State) state).getArray().length == t.getStateDimension()) {

            // out of all actions that exist you should choose the action, which maximizes the immediate reward + V(s)

            PlayerActionSetFactory a = new PlayerActionSetFactory();
            List<PlayerActionSet> permutations = new ArrayList<>();
            permutations = a.getReducedActions();

            int maxActionPermutation = 0;
            double maxValue = Double.MIN_VALUE;

            // parameter
            double gamma = 0.7;

            for (int i = 0; i < permutations.size(); i++) {

                double value = 0;
                State s = t.getNewStateSample((State) state, permutations.get(i), r.getPitchSide());

                value += r.calculateReward((State) state, permutations.get(i), s);
                value += gamma * v.interpolateReward(s);

                //logger.info("For Actioncombination " +  permutations.get(i) + " the reward would be " + value);

                if (maxValue < value) {
                    maxValue = value;
                    maxActionPermutation = i;
                }
            }
            for (int i = 0; i < playerControllers.size(); i++) {
                action.put(playerControllers.get(i), permutations.get(maxActionPermutation).getActions().get(i).getAction());
            }
        } else {
            action.put(playerControllers.get(0), new Turn(0));
            action.put(playerControllers.get(1), new Turn(0));
        }
        return action;
    }
}

package edu.kit.robocup.mdp.policy;

import cern.colt.matrix.DoubleMatrix1D;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.controller.IPlayerController;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.interf.mdp.IState;

import java.util.List;
import java.util.Map;

/**
 * Created by dani on 27.05.2016.
 */
public class ValueIterationPolicy implements IPolicy {

    private DoubleMatrix1D theta;

    public ValueIterationPolicy(DoubleMatrix1D theta) {
        this.theta = theta;
    }

    @Override
    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> playerControllers, PitchSide pitchSide) {
        //TODO out of all actions that exist you should choose the action, which maximizes the immediate reward + theta*nextState
        return null;
    }
}

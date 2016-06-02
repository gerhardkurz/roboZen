package edu.kit.robocup.interf.mdp;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.state.State;

/**
 * Created by dani on 02.06.2016.
 */
public interface IReward {

    public int calculateReward(State prevState, IActionSet action, State nextState);
    public PitchSide getPitchSide();
}

package edu.kit.robocup.mdp.transition;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.mdp.PlayerActionSet;

/**
 * Created by dani on 12.06.2016.
 */
public interface ITransition {
    public State getNewStateSample(State s, PlayerActionSet a, PitchSide pitchSide);
    public int getNumberAllPlayers();
    public int getStateDimension();
    public int getNumberPlayersPitchside();
}

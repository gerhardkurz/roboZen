package edu.kit.robocup.tree;


import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.interf.mdp.IState;

public interface IReward {
    int getReward(IState currentState, PitchSide pitchSide);
}

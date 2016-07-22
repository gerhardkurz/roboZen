package edu.kit.robocup.tree;


import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.interf.mdp.IState;

public interface IPruner {
    boolean prune(IState begin, IState end, PitchSide pitchSide);
}

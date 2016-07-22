package edu.kit.robocup.tree;


import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.interf.mdp.IState;
import edu.kit.robocup.mdp.PlayerActionSet;
import java.util.List;

public interface IComplexAction {




    // kick goal
    // pass
    // closer run to ball - parallel mit
    //
    boolean isPossible(IState state, PitchSide pitchSide);
    List<PlayerActionSet> getPlayerActionSets(IState state, PitchSide pitchSide);
}

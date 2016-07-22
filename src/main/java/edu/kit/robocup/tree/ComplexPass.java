package edu.kit.robocup.tree;


import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.interf.mdp.IState;
import edu.kit.robocup.mdp.PlayerActionSet;

import java.util.List;

public class ComplexPass implements IComplexAction {
    @Override
    public boolean isPossible(IState state, PitchSide pitchSide) {
        return false;
    }

    @Override
    public List<PlayerActionSet> getPlayerActionSets(IState state, PitchSide pitchSide) {
        return null;
    }
}

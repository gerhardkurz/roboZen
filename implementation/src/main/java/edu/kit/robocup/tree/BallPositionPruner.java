package edu.kit.robocup.tree;

import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.interf.mdp.IState;


public class BallPositionPruner implements IPruner {
    public boolean prune(IState begin, IState end, PitchSide pitchSide) {
        if (ballDistanceToOwnGoal(begin, pitchSide) < ballDistanceToOwnGoal(end, pitchSide)) {
            return true;
        } else {
            return false;
        }
    }

    private double ballDistanceToOwnGoal(IState state, PitchSide pitchSide) {
        double goalPosEast = Constants.PITCH_LENGTH / 2;
        double goalPosWest = -goalPosEast;
        if (pitchSide == PitchSide.EAST) {
            return state.getBall().getPositionX() - goalPosEast;
        } else {
            return state.getBall().getPositionX() - goalPosWest;
        }
    }

}

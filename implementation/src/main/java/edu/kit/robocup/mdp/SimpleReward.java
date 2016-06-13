package edu.kit.robocup.mdp;

import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.interf.mdp.IReward;

import java.util.List;

/**
 * Created by dani on 02.06.2016.
 */
public class SimpleReward implements IReward {

    private int goal;
    private PitchSide pitchSide;

    public SimpleReward(int goal, PitchSide pitchSide) {
        this.goal = goal;
        this.pitchSide = pitchSide;
    }

    public double calculateReward(State prevState, PlayerActionSet action, State nextState) {
        double reward = 0;
        Ball bnext = nextState.getBall();

        if (pitchSide.equals(PitchSide.EAST)) {
            if (bnext.getPositionX() <= Constants.GOAL_WEST.getLowerPost().getPositionX() && bnext.getPositionY() <= Constants.GOAL_WEST.getUpperPost().getPositionY() && bnext.getPositionY() >= Constants.GOAL_WEST.getLowerPost().getPositionY()) {
                reward += goal;
            }
        } else {
            if (bnext.getPositionX() >= Constants.GOAL_EAST.getLowerPost().getPositionX() && bnext.getPositionY() <= Constants.GOAL_EAST.getUpperPost().getPositionY() && bnext.getPositionY() >= Constants.GOAL_EAST.getLowerPost().getPositionY()) {
                reward += goal;
            }
        }

        return reward;
    }

    @Override
    public PitchSide getPitchSide() {
        return pitchSide;
    }

}

package edu.kit.robocup.tree;


import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.interf.mdp.IState;

public class TreeReward implements IReward {

    public int getReward(IState currentState, PitchSide pitchSide) {
        IState normalizedState = normalizeStateToEast(currentState, pitchSide);
        int reward = 0;
        reward += rewardPlayMode(normalizedState, pitchSide);

        return reward;
    }

    private IState normalizeStateToEast(IState currentState, PitchSide pitchSide) {
        IState normalizedState;
        if (pitchSide == PitchSide.WEST) {
            normalizedState = currentState.flipPitchSide();
        } else {
            normalizedState = currentState;
        }
        return normalizedState;
    }


    private int rewardPlayMode(IState currentState, PitchSide pitchSide) {
        switch(currentState.getPlayMode()) {
            case PLAY_ON:
                return 0;
            case GOAL_SIDE_EAST:
                return pitchSide == PitchSide.EAST? -10000:10000;
            case GOAL_SIDE_WEST:
                return pitchSide == PitchSide.EAST? 10000:-10000;
            case TIME_OVER:
            case KICK_OFF_EAST:
            case KICK_OFF_WEST:
            case BEFORE_KICK_OFF:
            case UNKNOWN:
            default:
                return 0;
        }
    }

    private int rewardBallPosition(IState currentState, PitchSide pitchSide) {
        int reward = 0;
        Ball ball = currentState.getBall();
        reward += ball.getPositionX();
        if ( ball.getPositionX() >= 35 ) {
            // if ball is near the goal
        }


        if (pitchSide == PitchSide.WEST)
            reward = -reward;
        return reward;
    }
}

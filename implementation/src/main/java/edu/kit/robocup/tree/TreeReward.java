package edu.kit.robocup.tree;


import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.interf.mdp.IState;

public class TreeReward implements IReward {

    public double getReward(IState currentState, PitchSide pitchSide) {
        IState normalizedState = currentState.normalizeStateToEast(pitchSide);
        double reward = 0;
        reward += rewardPlayMode(normalizedState, pitchSide);
        reward += rewardBallPosition(normalizedState);
        reward += rewardDistanceToBall(normalizedState, pitchSide);
        //reward += rewardSpreadOutPlayers(normalizedState, pitchSide);
        return reward;
    }

    private double rewardSpreadOutPlayers(IState normalizedState, PitchSide pitchSide) {
        double reward = 0;
        for (IPlayerState player : normalizedState.getPlayers(pitchSide)) {
            reward += rewardSpreadOutPlayer(player, normalizedState, pitchSide);
        }
        return reward;
    }


    private double rewardSpreadOutPlayer(IPlayerState player, IState normalizedState, PitchSide pitchSide) {
        final double distanceToSpreadOut = 10;
        final double rewardForSpreadOut = 100;
        double reward = 0;
        for (IPlayerState other : normalizedState.getPlayers(pitchSide)) {
            if (!player.equals(other)) {
                reward += player.getDistance(other) > distanceToSpreadOut ? rewardForSpreadOut : 0;
            }
        }
        return reward;
    }


    private double rewardPlayMode(IState currentState, PitchSide pitchSide) {
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

    private double rewardBallPosition(IState normalizedState) {
        double reward = 0;
        Ball ball = normalizedState.getBall();
        reward += distanceToReward(ball.getDistanceToGoal(false));
        reward += -500 * ball.getVelocityX();
        return reward;
    }

    private double rewardDistanceToBall(IState normalizedState, PitchSide pitchSide) {
        double reward = 0;
        Ball ball = normalizedState.getBall();
        for (IPlayerState player : normalizedState.getPlayers(pitchSide)) {
            reward += 0.25 * distanceToReward(ball.getDistance(player));
        }
        return reward;
    }

    private double distanceToReward(double distance) {
        return -(distance);
    }
}

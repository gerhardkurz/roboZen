package edu.kit.robocup.tree;

import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.interf.mdp.IState;

import java.util.List;


public class BallPositionPruner implements IPruner {
    public boolean prune(IState begin, IState end, PitchSide pitchSide) {
        if (ballDistanceToOwnGoal(begin, pitchSide) < ballDistanceToOwnGoal(end, pitchSide)) {
            return true;
        } else {
            List<IPlayerState> playersBefore = begin.getPlayers(pitchSide);
            List<IPlayerState> playersAfter = end.getPlayers(pitchSide);
            if (!getNearerToBall(playersBefore, playersAfter, end.getBall())) {
                return true;
            }
            return false;
        }
    }

    private boolean getNearerToBall(List<IPlayerState> before, List<IPlayerState> after, Ball b) {
        for(int i = 0; i < before.size(); i++) {
            if (before.get(i).getDistance(b) >= after.get(i).getDistance(b)) {
                return true;
            }
        }
        return false;
    }

    private double ballDistanceToOwnGoal(IState state, PitchSide pitchSide) {
        if (pitchSide == PitchSide.EAST) {
            return Constants.GOAL_EAST.getDistance(state.getBall());
        } else {
            return Constants.GOAL_WEST.getDistance(state.getBall());
        }
    }

}

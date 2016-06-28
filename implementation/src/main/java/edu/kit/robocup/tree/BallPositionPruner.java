package edu.kit.robocup.tree;

import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.interf.mdp.IState;
import org.apache.log4j.Logger;

import java.util.List;


public class BallPositionPruner implements IPruner {

    private static Logger logger = Logger.getLogger(BallPositionPruner.class.getName());

    public boolean prune(IState begin, IState end, PitchSide pitchSide) {
        /*if (ballDistanceToOwnGoal(begin, pitchSide) < ballDistanceToOwnGoal(end, pitchSide)) {
            logger.info("pruned because ball gets away from goal");
            return true;
        } else {*/
            List<IPlayerState> playersBefore = begin.getPlayers(pitchSide);
            List<IPlayerState> playersAfter = end.getPlayers(pitchSide);
            if (!getNearerToBall(playersBefore, playersAfter, end.getBall())) {
                return true;
            }
            return false;
        //}
    }

    private boolean getNearerToBall(List<IPlayerState> before, List<IPlayerState> after, Ball b) {
        for(int i = 0; i < before.size(); i++) {
            if ((before.get(i).getDistance(b) >= after.get(i).getDistance(b)) || (after.get(i).getVelocityLength() < 0.1)) {
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

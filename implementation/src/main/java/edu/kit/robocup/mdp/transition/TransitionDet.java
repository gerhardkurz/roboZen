package edu.kit.robocup.mdp.transition;

import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.mdp.PlayerActionSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dani on 12.06.2016.
 */
public class TransitionDet implements ITransition {
    @Override
    public State getNewStateSample(State s, PlayerActionSet a, PitchSide pitchSide) {
        Ball b = s.getBall();
        List<IPlayerState> newPlayers = new ArrayList<>();
        List<IPlayerState> p = s.getPlayers(pitchSide);
        for (IPlayerState player : p) {
            PlayerAction actual = null;
            for (PlayerAction act : a.getActions()) {
                if (act.getPlayerNumber() == player.getNumber()) {
                    actual = act;
                }
            }
            switch (actual.getActionType()) {
                case KICK: {
                    double dist_ball = player.getDistance(b);
                    if (dist_ball > Constants.KICKABLE_MARGIN) {
                        newPlayers.add(player);
                    } else {
                        double ep = actual.getAction().getArray()[0] * Constants.kick_power_rate;
                        double dir_diff = player.getAngleTo(b);
                        ep = (1 - 0.25* dir_diff/180.0 - 0.25* dist_ball/Constants.KICKABLE_MARGIN);
                    }
                    break;
                }
                case DASH: {
                    double aX = actual.getAction().getArray()[0] * Math.cos(Math.toRadians(player.getBodyAngle()));
                    double aY = actual.getAction().getArray()[0] * Math.sin(Math.toRadians(player.getBodyAngle()));
                    break;
                }
                case TURN: {
                    double theta = player.getBodyAngle() + actual.getAction().getArray()[0];
                    while (theta > 180) {
                        theta = theta - 360;
                    }
                    while (theta < -180) {
                        theta = theta + 360;
                    }
                    newPlayers.add(new PlayerState(player.getPitchSide(), player.getNumber(), player.getPositionX(), player.getPositionY(), player.getVelocityLength(), player.getBodyAngle(), player.getNeckAngle()));
                    break;
                }
                case MOVE: {
                    newPlayers.add(new PlayerState(player.getPitchSide(), player.getNumber(), actual.getAction().getArray()[0], actual.getAction().getArray()[1], player.getVelocityLength(), player.getBodyAngle(), player.getNeckAngle()));
                    break;
                }
            }
        }
        return null;
    }
}

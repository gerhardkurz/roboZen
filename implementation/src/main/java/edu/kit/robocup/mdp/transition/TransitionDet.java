package edu.kit.robocup.mdp.transition;

import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.Kick;
import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.mdp.PlayerActionSet;
import edu.kit.robocup.mdp.Reward;
import org.apache.commons.math3.analysis.function.Constant;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dani on 12.06.2016.
 */
public class TransitionDet implements ITransition {
    static Logger logger = Logger.getLogger(TransitionDet.class.getName());

    private int numberAllPlayers;
    private int stateDimension;
    private int numberPlayersPitchside;

    public TransitionDet(int numberPlayersPitchside, int numberAllPlayers, int stateDimension) {
        this.numberPlayersPitchside = numberPlayersPitchside;
        this.numberAllPlayers = numberAllPlayers;
        this.stateDimension = stateDimension;
    }

    @Override
    public State getNewStateSample(State s, PlayerActionSet a, PitchSide pitchSide) {
        Ball b = s.getBall();
        List<IPlayerState> newPlayers = new ArrayList<>();
        List<IPlayerState> p = s.getPlayers(pitchSide);
        double ballvx = 0;
        double ballvy = 0;
        double ballax = 0;
        double ballay = 0;
        for (IPlayerState player : p) {
            double vx = 0;
            double vy = 0;
            double ax = 0;
            double ay = 0;
            double deltaAngle = 0;
            double deltaPx = 0;
            double deltaPy = 0;
            IAction actual = null;
            for (PlayerAction act : a.getActions()) {
                if (act.getPlayerNumber() == player.getNumber()) {
                    actual = act.getAction();
                }
            }
            boolean move = false;
            switch (actual.getActionType()) {
                case KICK: {
                    double dist_ball = player.getDistance(b) - Constants.ball_size - Constants.player_size;
                    if (dist_ball <= Constants.KICKABLE_MARGIN) {
                        double ep = actual.getArray()[0] * Constants.kick_power_rate;
                        double dir_diff = player.getAngleTo(b);
                        if (dir_diff > 180) {
                            dir_diff = Math.abs(dir_diff -360);
                        } else if (dir_diff < -180) {
                            dir_diff = Math.abs(dir_diff +360);
                        }
                        ep = ep * (1 - 0.25* dir_diff/180.0 - 0.25* dist_ball/Constants.KICKABLE_MARGIN);
                        ballax += ep;
                        ballay += ep;
                    }
                    break;
                }
                case DASH: {
                    ax = actual.getArray()[0] *Constants.dash_power_rate* Math.cos(Math.toRadians(player.getBodyAngle()));
                    ay = actual.getArray()[0] *Constants.dash_power_rate* Math.sin(Math.toRadians(player.getBodyAngle()));
                    break;
                }
                case TURN: {
                    deltaAngle = actual.getArray()[0];
                    if (deltaAngle + player.getBodyAngle() >= 180) {
                        deltaAngle = deltaAngle - 360;
                    } else if (deltaAngle + player.getBodyAngle() <= -180) {
                        deltaAngle = deltaAngle +360;
                    }
                    break;
                }
                case MOVE: {
                    deltaPx = actual.getArray()[0];
                    deltaPy = actual.getArray()[1];
                    move = true;
                    break;
                }
            }
            if (move) {
                newPlayers.add(new PlayerState(player.getPitchSide(), player.getNumber(), deltaPx, deltaPy, 0, player.getBodyAngle(), player.getNeckAngle()));
            } else {
                double ux = player.getVelocityX() + ax;
                double uy = player.getVelocityY() + ay;
                double px = player.getPositionX() + ux;
                double py = player.getPositionY() + uy;
                vx = Constants.player_decay * ux;
                vy = Constants.player_decay * uy;
                newPlayers.add(new PlayerState(player.getPitchSide(), player.getNumber(), px, py, Math.sqrt(vx * vx + vy * vy), player.getBodyAngle() + deltaAngle, player.getNeckAngle()));
            }
        }
        double ballux = b.getVelocityX()+ballax;
        double balluy = b.getVelocityY()+ballay;
        double ballpx = b.getPositionX() + ballux;
        double ballpy = b.getPositionY() + balluy;
        ballvx = Constants.ball_decay * ballux;
        ballvy = Constants.ball_decay * balluy;
        if (pitchSide == PitchSide.EAST) {
            newPlayers.addAll(s.getPlayers(PitchSide.WEST));
        } else {
            newPlayers.addAll(s.getPlayers(PitchSide.EAST));
        }
        return new State(new Ball(ballpx, ballpy, ballvx, ballvy), newPlayers);
    }

    @Override
    public int getNumberAllPlayers() {
        return this.numberAllPlayers;
    }

    @Override
    public int getStateDimension() {
        return this.stateDimension;
    }

    @Override
    public int getNumberPlayersPitchside() {
        return this.numberPlayersPitchside;
    }

    public static void main(String[] args) {
        List<IPlayerState> p = new ArrayList<>();
        p.add(new PlayerState(PitchSide.EAST, 1, 0, 0));
        p.add(new PlayerState(PitchSide.EAST, 2, 0, 0));
        p.add(new PlayerState(PitchSide.WEST, 1, 0, 0));
        p.add(new PlayerState(PitchSide.WEST, 2, 0, 0));
        State s = new State(new Ball(-20, 0, 3, 4), p);
        List<PlayerAction> l = new ArrayList<>();
        l.add(new PlayerAction(1, new Kick(30, 0)));
        l.add(new PlayerAction(2, new Kick(0, 30)));
        PlayerActionSet a = new PlayerActionSet(l);
        TransitionDet t = new TransitionDet(2, 4, 20);
        logger.info(t.getNewStateSample(s, a, PitchSide.EAST));
    }
}

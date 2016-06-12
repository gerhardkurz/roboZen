package edu.kit.robocup.game;

import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IPlayerState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class StateFactory {

    public StateFactory() {};

    public State getRandomState(int numberPlayers, PitchSide pitchSide) {
        Ball ball = new Ball(getRandomXPosition(), getRandomYPosition(), getRandomVelocity(), getRandomVelocity());
        List<IPlayerState> p = new ArrayList<>();
        for (int i = 0; i < numberPlayers; i++) {
            p.add(new PlayerState(pitchSide, i, getRandomXPosition(), getRandomYPosition(), getRandomVelocityLength(), getRandomDouble(Constants.minmoment, Constants.maxmoment), getRandomDouble(Constants.minneckmoment, Constants.maxneckmoment)));
        }
        return new State(ball, p);
    }

    private double getRandomVelocity() {
        return getRandomDouble(-Constants.kick_power_rate * Constants.maxpower, Constants.kick_power_rate * Constants.maxpower);
    }

    private double getRandomVelocityLength() {
        double x = getRandomVelocity();
        double y = getRandomVelocity();
        return Math.sqrt(x*x+y*y);
    }

    private double getRandomXPosition() {
        return getRandomDouble(-Constants.PITCH_LENGTH/2.0, Constants.PITCH_LENGTH/2.0);
    }

    private double getRandomYPosition() {
        return getRandomDouble(-Constants.PITCH_WIDTH/2.0, Constants.PITCH_WIDTH/2.0);
    }

    private double getRandomDouble(double min, double max) {
        Random r = new Random();
        double randomValue = min + (max - min) * r.nextDouble();
        return randomValue;
    }

    public static void main(String[] args) {
        StateFactory s = new StateFactory();
        State state = s.getRandomState(3, PitchSide.WEST);
        System.out.println(state.toString());
    }

}

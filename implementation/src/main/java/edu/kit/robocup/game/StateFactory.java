package edu.kit.robocup.game;

import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IPlayerState;
import org.jsoup.select.Collector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;


public class StateFactory {

    public StateFactory() {};

    public List<State> getEquidistantStates(int numberPlayersPitchside, int numberAllPlayers, PitchSide pitchSide, int positionResolution, int velocityResolution) {

        //int[] dimension = [positionResolution, positionResolution];

        List<State> states = LongStream.range(0, positionResolution * 2 * velocityResolution * numberAllPlayers)
                .mapToObj(i -> getEquidistantState(numberPlayersPitchside, numberAllPlayers, pitchSide, positionResolution, velocityResolution, i))
                .collect(Collectors.toList();

        return states;
    }

    private State getEquidistantState(int numberPlayersPitchside, int numberAllPlayers, PitchSide pitchSide, int positionResolution, int velocityResolution, long index) {

        return null;
    }

    public State getRandomState(int numberPlayersPitchside, int numberAllPlayers, PitchSide pitchSide) {
        Ball ball = new Ball(getRandomXPosition(), getRandomYPosition(), getRandomVelocity(), getRandomVelocity());
        List<IPlayerState> p = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < numberAllPlayers; i++) {
            counter++;
            if (counter <= numberPlayersPitchside) {
                p.add(new PlayerState(pitchSide, i + 1, getRandomXPosition(), getRandomYPosition(), getRandomVelocityLength(), getRandomDouble(Constants.minmoment, Constants.maxmoment), getRandomDouble(Constants.minneckmoment, Constants.maxneckmoment)));
            } else {
                if (pitchSide == PitchSide.EAST) {
                    p.add(new PlayerState(PitchSide.WEST, i + 1 - numberPlayersPitchside, getRandomXPosition(), getRandomYPosition(), getRandomVelocityLength(), getRandomDouble(Constants.minmoment, Constants.maxmoment), getRandomDouble(Constants.minneckmoment, Constants.maxneckmoment)));
                } else {
                    p.add(new PlayerState(PitchSide.EAST, i + 1 - numberPlayersPitchside, getRandomXPosition(), getRandomYPosition(), getRandomVelocityLength(), getRandomDouble(Constants.minmoment, Constants.maxmoment), getRandomDouble(Constants.minneckmoment, Constants.maxneckmoment)));
                }
            }
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

}

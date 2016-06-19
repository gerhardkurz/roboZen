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

    final static double playFieldWidth = 100;
    final static double playFieldHeight = 500;
    final static double maxVelocity = 10;

    public List<State> getEquidistantStates(int numberPlayersPitchside, int numberAllPlayers, PitchSide pitchSide, int positionResolution, int rotationResolution, int velocityResolution) {

        // Distances
        double positionDistanceX = playFieldWidth / (positionResolution - 1);
        double positionDistanceY = playFieldHeight / (positionResolution - 1);
        double velocityDistance = maxVelocity / (velocityResolution - 1);
        double rotationDistance = 360 / (rotationResolution - 1);

        // prepare Arrays
        int dimensions = 4 + (numberAllPlayers * 5);
        double[] stepDistance = new double[ dimensions ];
        int[] maxSteps = new int[ dimensions ];
        double[] currentSteps = new double[ dimensions ];

        stepDistance[0] = positionDistanceX;
        stepDistance[1] = positionDistanceY;
        stepDistance[2] = velocityDistance;
        stepDistance[3] = velocityDistance;

        maxSteps[0] = positionResolution;
        maxSteps[1] = positionResolution;
        maxSteps[2] = velocityResolution;
        maxSteps[3] = velocityResolution;

        for (int player = 0; player < numberAllPlayers; player++) {
            int pOffset = 4 + (player * 5);
            stepDistance[pOffset] = positionDistanceX;
            stepDistance[pOffset + 1] = positionDistanceY;
            stepDistance[pOffset + 2] = velocityDistance;
            stepDistance[pOffset + 3] = velocityDistance;
            stepDistance[pOffset + 4] = rotationDistance;

            maxSteps[pOffset] = positionResolution;
            maxSteps[pOffset + 1] = positionResolution;
            maxSteps[pOffset + 2] = velocityResolution;
            maxSteps[pOffset + 3] = velocityResolution;
            maxSteps[pOffset + 4] = rotationResolution;
        }

        // generate States
        ArrayList<State> states = new ArrayList<>();
        provideStatesRecursive(0, stepDistance, maxSteps, currentSteps, states, numberPlayersPitchside, numberAllPlayers, pitchSide);

        return states;
    }

    private void provideStatesRecursive(int dimension, double[] stepDistance, int[] maxSteps, double[] currentDistance, List<State> states, int numberPlayersPitchside, int numberAllPlayers, PitchSide pitchSide) {
        if (dimension == currentDistance.length) {
            states.add(new State(currentDistance, numberPlayersPitchside, numberAllPlayers, pitchSide));
        }
        for (int i = 0; i < maxSteps[dimension]; i++) {
            currentDistance[dimension] = i * stepDistance[dimension];
            provideStatesRecursive(dimension + 1, stepDistance, maxSteps, currentDistance, states, numberPlayersPitchside, numberAllPlayers, pitchSide);
        }
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

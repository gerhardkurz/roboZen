package edu.kit.robocup.game;

import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.controller.IPlayerController;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.recorder.Sandbox;
import org.apache.log4j.Logger;
import org.jsoup.select.Collector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;


public class StateFactory {

    static Logger logger = Logger.getLogger(StateFactory.class.getName());
    public StateFactory() {};

    final static double playFieldWidth = 100;
    final static double playFieldHeight = 500;
    final static double maxVelocity = 10;

    public static List<State> getEquidistantStates(int numberPlayersPitchside, int numberAllPlayers, PitchSide pitchSide, int positionResolution, int rotationResolution, int velocityResolution) {

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

    private static void provideStatesRecursive(int dimension, double[] stepDistance, int[] maxSteps, double[] currentDistance, List<State> states, int numberPlayersPitchside, int numberAllPlayers, PitchSide pitchSide) {
        if (dimension == currentDistance.length) {
            State state = new State(currentDistance, numberPlayersPitchside, numberAllPlayers, pitchSide);
            states.add(state);
            //logger.info("created state: " + state);
            return;
        }
        for (int i = 0; i < maxSteps[dimension]; i++) {
            currentDistance[dimension] = i * stepDistance[dimension];
            provideStatesRecursive(dimension + 1, stepDistance, maxSteps, currentDistance, states, numberPlayersPitchside, numberAllPlayers, pitchSide);
        }
    }

    // euler distance
    public static double distance(State s, State q) {
        double toSqrt = 0;
        for (int i = 0; i < s.getDimension(); i++) {
            toSqrt += (s.getArray()[i] - q.getArray()[i]) * (s.getArray()[i] - q.getArray()[i]);
        }
        return Math.sqrt(toSqrt);
    }

    public static State reduceState(State s, int positionResolution, int rotationResolution, int velocityResolution) {
        List<IPlayerState> p = s.getPlayers();
        List<IPlayerState> newP = new ArrayList<>();
        for (IPlayerState player : p) {
            double posX = reducePositionX(positionResolution, player.getPositionX());
            double posY = reducePositionY(positionResolution, player.getPositionY());
            double vel = reduceVelocity(velocityResolution, player.getVelocityLength());
            double angle = reduceAngle(rotationResolution, player.getBodyAngle());
            newP.add(new PlayerState(player.getPitchSide(), player.getNumber(), posX, posY, vel, angle, player.getNeckAngle()));
        }
        double posX = reducePositionX(positionResolution, s.getBall().getPositionX());
        double posY = reducePositionX(positionResolution, s.getBall().getPositionY());
        double velX = reduceVelocity(rotationResolution, s.getBall().getVelocityX());
        double velY = reduceVelocity(rotationResolution, s.getBall().getVelocityY());
        return new State(new Ball(posX, posY, velX, velY), newP);
    }

    private static double reducePositionX(int positionResolution, double position) {
        double step = (playFieldWidth/(positionResolution-1));
        return position - (position % step);
    }
    private static double reducePositionY(int positionResolution, double position) {
        double step = (playFieldHeight/(positionResolution-1));
        return position - (position % step);
    }
    private static double reduceVelocity(int velocityResolution, double vel) {
        double step = (maxVelocity/(velocityResolution-1));
        return vel - (vel % step);
    }
    private static double reduceAngle(int rotationResolution, double angle) {
        double a = angle + 180;
        double step = (360/(rotationResolution -1));
        a = a - (a % step);
        return (a-180);
    }

    public static State increaseState(State s, int positionResolution, int rotationResolution, int velocityResolution) {
        List<IPlayerState> p = s.getPlayers();
        List<IPlayerState> newP = new ArrayList<>();
        for (IPlayerState player : p) {
            double posX = increasePositionX(positionResolution, player.getPositionX());
            double posY = increasePositionY(positionResolution, player.getPositionY());
            double vel = increaseVelocity(velocityResolution, player.getVelocityLength());
            double angle = increaseAngle(rotationResolution, player.getBodyAngle());
            newP.add(new PlayerState(player.getPitchSide(), player.getNumber(), posX, posY, vel, angle, player.getNeckAngle()));
        }
        double posX = increasePositionX(positionResolution, s.getBall().getPositionX());
        double posY = increasePositionX(positionResolution, s.getBall().getPositionY());
        double velX = increaseVelocity(rotationResolution, s.getBall().getVelocityX());
        double velY = increaseVelocity(rotationResolution, s.getBall().getVelocityY());
        return new State(new Ball(posX, posY, velX, velY), newP);
    }

    private static double increasePositionX(int positionResolution, double position) {
        double step = (playFieldWidth/(positionResolution-1));
        return position + (step - (position % step));
    }
    private static double increasePositionY(int positionResolution, double position) {
        double step = (playFieldHeight/(positionResolution-1));
        return position + (step - (position % step));
    }
    private static double increaseVelocity(int velocityResolution, double vel) {
        double step = (maxVelocity/(velocityResolution-1));
        return vel + (step -(vel % step));
    }
    private static double increaseAngle(int rotationResolution, double angle) {
        double a = angle + 180;
        double step = 360/(rotationResolution-1);
        a = a + (step-(a % step));
        return (a-180);
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

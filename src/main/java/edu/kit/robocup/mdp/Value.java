package edu.kit.robocup.mdp;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.StateFactory;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.interf.mdp.IReward;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dani on 20.06.2016.
 */
public class Value {


    static Logger logger = Logger.getLogger(Value.class.getName());

    private Map<State, Double> rewards;
    private final static double playFieldWidth = 100;
    private final static double playFieldHeight = 500;
    private final static double maxVelocity = 10;
    private int positionResolution;
    private int rotationResolution;
    private int velocityResolution;

    public Value (int positionResolution, int rotationResolution, int velocityResolution) {
        this.rewards = new HashMap<>();
        this.positionResolution = positionResolution;
        this.rotationResolution = rotationResolution;
        this.velocityResolution = velocityResolution;
    }

    public Value(Map<State, Double> rewards, int positionResolution, int rotationResolution, int velocityResolution) {
        this.rewards = rewards;
        this.positionResolution = positionResolution;
        this.rotationResolution = rotationResolution;
        this.velocityResolution = velocityResolution;
    }

    public void setRewards(Map<State, Double> rewards) {
        this.rewards = rewards;
    }

    public double interpolateReward(State s) {
        State reduced = StateFactory.reduceState(s, positionResolution, rotationResolution, velocityResolution);
        State increased = StateFactory.increaseState(s, positionResolution, rotationResolution, velocityResolution);
        double[] red = reduced.getArray();
        double[] inc = increased.getArray();

        List<State> neighbors = new ArrayList<>();

        for (int i = 0; i < Math.pow(2, red.length); i++) {
            double[] newState = new double[red.length];
            for (int j = 0; j < red.length; j++) {
                String dual = Integer.toBinaryString(i);
                if (dual.length() > j) {
                    if (Integer.toBinaryString(i).charAt(j) == 0) {
                        newState[j] = red[j];
                    } else {
                        newState[j] = inc[j];
                    }
                } else {
                    newState[j] = red[j];
                }
            }
            neighbors.add(new State(newState, PitchSide.EAST, s.getPlayers(PitchSide.EAST).size()));
        }
        double reward = 0;
        double allDistances = 0;
        for (int i = 0; i < neighbors.size(); i++) {
            double distance = StateFactory.distance(s, neighbors.get(i));
            if (distance < 500) {
                allDistances += distance;
                // inverse distance weighting
                logger.info(neighbors.get(i));
                reward += 1 / distance * rewards.get(neighbors.get(i));
            }
        }
        logger.info(neighbors.size());
        if (allDistances != 0) {
            return (reward / (allDistances));
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        Map<State, Double> m = new HashMap<>();
        List<IPlayerState> p = new ArrayList<>();
        p.add(new PlayerState(PitchSide.EAST, 1, 0, 0));
        p.add(new PlayerState(PitchSide.WEST, 2, 0, 0));
        State s = new State(new Ball(0, 0), p);
        m.put(s, 2.0);

        p = new ArrayList<>();
        p.add(new PlayerState(PitchSide.EAST, 1, 0, 5));
        p.add(new PlayerState(PitchSide.WEST, 2, 0, 0));
        s = new State(new Ball(0, 0), p);
        m.put(s, 4.0);

        p = new ArrayList<>();
        p.add(new PlayerState(PitchSide.EAST, 1, 5, 0));
        p.add(new PlayerState(PitchSide.WEST, 2, 0, 0));
        s = new State(new Ball(0, 0), p);
        m.put(s, 8.0);

        p = new ArrayList<>();
        p.add(new PlayerState(PitchSide.EAST, 1, 5, 5));
        p.add(new PlayerState(PitchSide.WEST, 2, 0, 0));
        s = new State(new Ball(0, 0), p);
        m.put(s, 6.0);

        Value v = new Value(m, 2, 2, 2);
    }
}

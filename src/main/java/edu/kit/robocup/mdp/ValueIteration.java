package edu.kit.robocup.mdp;

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.*;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.interf.mdp.IReward;
import edu.kit.robocup.interf.mdp.ISolver;
import edu.kit.robocup.mdp.policy.ValueIterationPolicy;
import edu.kit.robocup.mdp.transition.Game;
import edu.kit.robocup.mdp.transition.ITransition;
import edu.kit.robocup.mdp.transition.Transition;
import edu.kit.robocup.mdp.transition.TransitionDet;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValueIteration implements ISolver {

    static Logger logger = Logger.getLogger(ValueIteration.class.getName());
    private ITransition t;
    private IReward r;
    private Value v;
    private int positionResolution;
    private int rotationResolution;
    private int velocityResolution;

    /*public ValueIteration(IReward r) {
        this.r = r;
    }*/

    public ValueIteration(List<Game> games, IReward r, int positionResolution, int rotationResolution, int velocityResolution) {
        this.t = new Transition(games);
        this.r = r;
        this.v = new Value(positionResolution, rotationResolution, velocityResolution);
        this.positionResolution = positionResolution;
        this.rotationResolution = rotationResolution;
        this.velocityResolution = velocityResolution;
    }

    public ValueIteration(ITransition t, IReward r, int positionResolution, int rotationResolution, int velocityResolution) {
        this.t = t;
        this.r = r;
        this.v = new Value(positionResolution, rotationResolution, velocityResolution);
        this.positionResolution = positionResolution;
        this.rotationResolution = rotationResolution;
        this.velocityResolution = velocityResolution;
    }

    /**
     * calculates IPolicy given the transition model and a reward function
     * @return best Policy
     */
    @Override
    public IPolicy solve() {
        PlayerActionSetFactory a = new PlayerActionSetFactory();
        // get all possible actions
        List<PlayerActionSet> permutations = a.getReducedActions();

        List<State> samples = StateFactory.getEquidistantStates(t.getNumberPlayersPitchside(), t.getNumberAllPlayers(), r.getPitchSide(), positionResolution, rotationResolution, velocityResolution);

        //parameters
        int numberSamples = samples.size();
        double gamma = 0.9;

        // iterate
        for (int horizon = 0; horizon < 1000; horizon++) {
            logger.info("horizon: " + horizon);
            Map<State, Double> currentRewards = new HashMap<>();

            for (int n = 0; n < numberSamples; n++) {
                // saves rewards for each action
                double[] q = new double[permutations.size()];

                double max = Double.MIN_VALUE;

                // iterate over all possible actions
                for (int act = 0; act < permutations.size(); act++) {

                    State s = t.getNewStateSample(samples.get(n), permutations.get(act), r.getPitchSide());

                    double reward = 0;

                    reward += r.calculateReward(samples.get(n), permutations.get(act), s);

                    if (horizon > 0) {
                        reward += gamma * v.interpolateReward(s);
                    }

                    q[act] = reward;

                    if (max < q[act]) {
                        max = q[act];
                    }
                }
                currentRewards.put(samples.get(n), max);
            }
            v.setRewards(currentRewards);
        }
        return new ValueIterationPolicy(v, r, t);
    }

    public static void main(String[] args) {
        /*List<State> samples = new ArrayList<>();
        double[] t = new double[]{1,2,3,4};
        State s = new State(t, PitchSide.EAST, 2);
        samples.add(s);
        t = new double[]{0, 3, 2, 4};
        s = new State(t, PitchSide.EAST, 2);
        samples.add(s);
        t = new double[]{1, 2, 2, 5};
        s = new State(t, PitchSide.EAST, 2);
        samples.add(s);
        t = new double[]{1, 3, 3, 4};
        s = new State(t, PitchSide.EAST, 2);
        samples.add(s);*/

        ValueIteration v = new ValueIteration(new TransitionDet(1, 1, 8),new Reward(2000,-2000,50, -50, 70, 170, -170, PitchSide.EAST), 2, 2, 2);
        v.solve();

        //double[] solution = v.getRegression(samples, new double[]{1, 2, 3, 5});
    }
}

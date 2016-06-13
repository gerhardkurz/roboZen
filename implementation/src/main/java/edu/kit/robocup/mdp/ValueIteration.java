package edu.kit.robocup.mdp;

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
import edu.kit.robocup.mdp.transition.Transition;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ValueIteration implements ISolver {

    static Logger logger = Logger.getLogger(ValueIteration.class.getName());
    private Transition t;
    private IReward r;

    public ValueIteration(List<Game> games, IReward r) {
        this.t = new Transition(games);
        this.r = r;
    }

    public ValueIteration(Transition t, IReward r) {
        this.t = t;
        this.r = r;
    }

    /**
     * calculates IPolicy given the transition modell and a reward function
     * @return best Policy
     */
    @Override
    public IPolicy solve() {
        PlayerActionSetFactory a = new PlayerActionSetFactory();
        List<PlayerActionSet> permutations = new ArrayList<>();
        permutations = a.getReducedActions();
        /*for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                permutations.addAll(a.getActionAction(i, j));
            }
        }*/
        int numberSamples = 10000;
        double gamma = 0.4;
        int K = 20;
        if (t.getA() == null) {
            t.startLearning();
        }
        List<State> samples = new ArrayList<>();
        StateFactory f = new StateFactory();
        for (int i = 0; i < numberSamples; i++) {
            samples.add(f.getRandomState(t.getGames().get(0).getNumberofAllPlayers(), r.getPitchSide()));
        }
        DoubleFactory1D h = DoubleFactory1D.dense;
        DoubleMatrix1D theta = h.make(t.getGames().get(0).getStates().get(0).getDimension(), 0);
        for (int horizon = 0; horizon < 800; horizon++) {
            logger.info("horizon: " + horizon);
            double[]  y = new double[numberSamples];
            for (int n = 0; n < numberSamples; n++) {
                double[] q = new double[permutations.size()];
                double max = 0;
                for (int act = 0; act < permutations.size(); act++) {
                    //saves K states, that could be the next states after beeing in state samples[i] and doing action act
                    List<State> resultingSamples = new ArrayList<>();
                    for (int k = 0; k < K; k++) {
                        State s = t.getNewStateSample(samples.get(n), permutations.get(act), r.getPitchSide());
                        resultingSamples.add(s);
                    }
                    double reward = 0;
                    for (int i = 0; i < K; i++) {
                        //logger.info("Prev: " + samples.get(n));
                        //logger.info("Next: " + resultingSamples.get(i));
                        reward += r.calculateReward(samples.get(n), permutations.get(act), resultingSamples.get(i));
                        //logger.info(reward);
                        /*if (horizon > 0) {
                            logger.info(reward);
                        }*/
                        reward += gamma * theta.zDotProduct(h.make(resultingSamples.get(i).getArray()));
                        /*if (horizon > 0) {
                            logger.info("Thetaproduct: " + theta.zDotProduct(h.make(resultingSamples.get(i).getArray())));
                        }*/
                    }
                    q[act] = 1/(K*1.0) * reward;
                    if (max < q[act]) {
                        max = q[act];
                    }
                }
                y[n] = max;
            }
            theta = h.make(getRegression(samples, y));
            logger.info("Theta is: " + theta.toString());
        }
        logger.info("Theta is: " + theta.toString());
        return new ValueIterationPolicy(theta, r, t);
    }

    // get best theta: y_i should be theta*samples_i
    private double[] getRegression(List<State> samples, double[] y) {
        DoubleFactory2D hh = DoubleFactory2D.dense;
        double[][] sampmatrix = new double[samples.size()][samples.get(0).getDimension()];
        for (int i = 0; i < samples.size(); i++) {
            sampmatrix[i] = samples.get(i).getArray();
        }
        DoubleMatrix2D M = hh.make(sampmatrix);
        DoubleFactory1D h = DoubleFactory1D.dense;
        DoubleMatrix1D b = h.make(y);
        // M*x = b is solved by x = (M^T*M)^-1 * M^T * b
        Algebra a = new Algebra();
        DoubleMatrix2D hasToInvert = a.mult(M.viewDice(), M);
        DoubleMatrix2D nearlyDone = a.mult(a.inverse(hasToInvert), M.viewDice());
        DoubleMatrix1D solution = h.make(samples.get(0).getDimension());
        nearlyDone.zMult(b, solution);
        //logger.info(solution.toString());
        return solution.toArray();
    }

    public static void main(String[] args) {
        List<State> samples = new ArrayList<>();
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
        samples.add(s);
        //ValueIteration v = new ValueIteration();
        //double[] solution = v.getRegression(samples, new double[]{1, 2, 3, 5});
    }
}

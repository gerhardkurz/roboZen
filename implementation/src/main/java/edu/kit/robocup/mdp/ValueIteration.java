package edu.kit.robocup.mdp;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import edu.kit.robocup.Main;
import edu.kit.robocup.game.*;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.interf.mdp.IActionSet;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.interf.mdp.ISolver;
import edu.kit.robocup.mdp.policy.ValueIterationPolicy;
import edu.kit.robocup.mdp.transition.Game;
import edu.kit.robocup.mdp.transition.Transitions;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dani on 26.05.2016.
 */
public class ValueIteration implements ISolver {

    static Logger logger = Logger.getLogger(Main.class.getName());
    private Transitions t;
    private Reward r;

    public ValueIteration(List<Game> games, Reward r) {
        this.t = new Transitions(games);
        this.r = r;
    }

    /**
     * calculates IPolicy given the transition modell and a reward function
     * @return best Policy
     */
    @Override
    public IPolicy solve() {
        List<IAction> reducedActions = new ArrayList<>();
        reducedActions.add(new Kick(30,0));
        reducedActions.add(new Kick(30,25));
        reducedActions.add(new Kick(30,-25));
        reducedActions.add(new Turn(1));
        reducedActions.add(new Turn(10));
        reducedActions.add(new Turn(50));
        reducedActions.add(new Turn(-1));
        reducedActions.add(new Turn(-10));
        reducedActions.add(new Turn(-50));
        reducedActions.add(new Dash(40));
        List<IActionSet> permutations = new ArrayList<>();
        for (int i = 0; i < reducedActions.size(); i++) {
            for (int j = 0; j < reducedActions.size(); j++) {
                List<IAction> t = new ArrayList<>();
                t.add(reducedActions.get(i));
                t.add(reducedActions.get(j));
                permutations.add(new ActionSet(t));
            }
        }
        int numberSamples = 1000;
        double gamma = 0.7;
        int K = 10;
        t.startLearning();
        List<State> samples = new ArrayList<>();
        StateFactory f = new StateFactory();
        for (int i = 0; i < numberSamples; i++) {
            samples.add(f.getRandomState(t.getGames().get(0).getNumberPlayers(), r.getTeamname()));
        }
        DoubleFactory1D h = DoubleFactory1D.dense;
        DoubleMatrix1D theta = h.make(t.getGames().get(0).getStates().get(0).getDimension(), 0);
        for (int horizon = 0; horizon < 1000; horizon++) {
            double[]  y = new double[numberSamples];
            for (int n = 0; n < numberSamples; n++) {
                double[] q = new double[Action.values().length];
                double max = 0;
                for (int act = 0; act < permutations.size(); act++) {
                    //TODO iterate over all actions and add them to the getNewStateSample method as parameter
                    //saves K states, that could be the next states after beeing in state samples[i] and doing action act
                    List<State> resultingSamples = new ArrayList<>();
                    for (int k = 0; k < K; k++) {
                        State s = t.getNewStateSample(samples.get(n), permutations.get(act), r.getTeamname());
                        resultingSamples.add(s);
                    }
                    double reward = 0;
                    for (int i = 0; i < K; i++) {
                        reward += r.calculateReward(samples.get(n), permutations.get(act), resultingSamples.get(i));
                        reward += gamma * theta.zDotProduct(h.make(resultingSamples.get(i).getArray()));
                    }
                    q[act] = 1/(K*1.0) * reward;
                    if (max < q[act]) {
                        max = q[act];
                    }
                }
                y[n] = max;
            }
            theta = h.make(getRegression(samples, y));
        }
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
        DoubleMatrix1D solution = h.make(samples.size());
        nearlyDone.zMult(b, solution);
        //logger.info(solution.toString());
        return solution.toArray();
    }

    public static void main(String[] args) {
        List<State> samples = new ArrayList<>();
        double[] t = new double[]{1,2,3,4};
        State s = new State(t, "dummy");
        samples.add(s);
        t = new double[]{0, 3, 2, 4};
        s = new State(t, "dummy");
        samples.add(s);
        t = new double[]{1, 2, 2, 5};
        s = new State(t, "dummy");
        samples.add(s);
        t = new double[]{1, 3, 3, 4};
        s = new State(t, "dummy");
        samples.add(s);
        //ValueIteration v = new ValueIteration();
        //double[] solution = v.getRegression(samples, new double[]{1, 2, 3, 5});
    }
}

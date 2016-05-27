package edu.kit.robocup.mdp;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import edu.kit.robocup.game.Action;
import edu.kit.robocup.game.StateFactory;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.interf.mdp.ISolver;
import edu.kit.robocup.mdp.transition.Game;
import edu.kit.robocup.mdp.transition.Transitions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dani on 26.05.2016.
 */
public class ValueIteration implements ISolver {

    private Transitions t;
    private Reward r;

    public ValueIteration(List<Game> games, Reward r) {
        this.t = new Transitions(games);
        this.r = r;
    }

    @Override
    public IPolicy solve() {
        int numberSamples = 1000;
        int K = 10;
        t.startLearning();
        List<State> samples = new ArrayList<>();
        StateFactory f = new StateFactory();
        for (int i = 0; i < numberSamples; i++) {
            samples.add(f.getRandomState(t.getGames().get(0).getNumberPlayers()));
        }
        DoubleFactory1D h = DoubleFactory1D.dense;
        DoubleMatrix1D theta = h.make(t.getGames().get(0).getStates().get(0).getDimension(), 0);
        for (int horizon = 0; horizon < 1000; horizon++) {
            double[]  y = new double[numberSamples];
            for (int n = 0; n < numberSamples; n++) {
                double[] q = new double[Action.values().length];
                double max = 0;
                for (int act = 0; act < Action.values().length; act++) {
                    //TODO iterate over all actions and add them to the getNewStateSample method as parameter
                    ActionSet a = new ActionSet(new ArrayList<IAction>());
                    //saves K states, that could be the next states after beeing in state samples[i] and doing action act
                    List<State> resultingSamples = new ArrayList<>();
                    for (int k = 0; k < K; k++) {
                        State s = t.getNewStateSample(samples.get(n), a);
                        resultingSamples.add(s);
                    }
                    double reward = 0;
                    for (int i = 0; i < K; i++) {
                        //TODO teamname has to be correct, random state has all players as dummies, isEast has to be correct
                        reward += r.calculateReward(samples.get(n), a, resultingSamples.get(i), "dummy", true);
                        reward += theta.zDotProduct(h.make(resultingSamples.get(i).getArray()));
                    }
                    q[act] = 1/(K*1.0) * reward;
                    if (max < q[act]) {
                        max = q[act];
                    }
                }
                y[n] = max;
            }
            //TODO update theta: y_i should be theta*samples_i
        }

        return null;
    }
}

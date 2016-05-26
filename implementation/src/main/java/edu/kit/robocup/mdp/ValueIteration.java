package edu.kit.robocup.mdp;

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
        t.startLearning();
        List<double[]> samples = t.getSamples(numberSamples);
        for (int horizon = 0; horizon < 1000; horizon++) {
            for (int n = 0; n < numberSamples; n++) {
                double[] actsample = samples.get(n);
            }
        }
        return null;
    }
}

package edu.kit.robocup.mdp.transition;

import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.mdp.IActionSet;
import edu.kit.robocup.mdp.ActionSet;

import java.util.Arrays;

/**
 * Created by dani on 29.05.2016.
 */
public class Information {
    private IActionSet a;
    private State s;

    public Information(State s, IActionSet a) {
        this.a = a;
        this.s = s;
    }

    public IActionSet getActionSet() {
        return this.a;
    }

    public State getState() {
        return this.s;
    }

    public boolean equals(Object obj) {
        Information other = (Information) obj;
        double[] athis = a.getArray();
        double[] sthis = s.getArray();
        double[] aother = other.getActionSet().getArray();
        double[] sother = other.getState().getArray();
        if (Arrays.equals(athis,aother) && Arrays.equals(sthis,sother)) {
            return true;
        } else {
            return false;
        }
    }
}

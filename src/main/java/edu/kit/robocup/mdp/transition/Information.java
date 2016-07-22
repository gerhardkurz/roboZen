package edu.kit.robocup.mdp.transition;

import edu.kit.robocup.game.state.State;
import edu.kit.robocup.mdp.PlayerActionSet;

import java.util.Arrays;



public class Information {
    private PlayerActionSet a;
    private State s;

    public Information(State s, PlayerActionSet a) {
        this.a = a;
        this.s = s;
    }

    public PlayerActionSet getActionSet() {
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

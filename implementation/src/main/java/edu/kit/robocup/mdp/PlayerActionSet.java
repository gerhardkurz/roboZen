package edu.kit.robocup.mdp;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import edu.kit.robocup.game.PlayerAction;

import static com.google.common.primitives.Doubles.concat;

public class PlayerActionSet implements Serializable {

    private List<PlayerAction> actions;
    private int dimension;

    public PlayerActionSet(List<PlayerAction> actions) {
        actions.sort(new Comparator<PlayerAction>() {
            @Override
            public int compare(PlayerAction o1, PlayerAction o2) {
                if (o1.getPlayerNumber() < o2.getPlayerNumber()) {
                    return -1;
                } else if (o1.getPlayerNumber() == o2.getPlayerNumber()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        this.actions = actions;
        int dim = 0;
        for (PlayerAction action: actions) {
            dim += action.getAction().getActionDimension();
        }
        this.dimension = dim;
    }

    public List<PlayerAction> getActions() {
        return this.actions;
    }

    public double[] getArray() {
        double[] a = new double[0];
        for (int i = 0; i < actions.size(); i++) {
            double[] act = actions.get(i).getArray();
            a = concat(a, act);
        }
        return a;
    }

    public int[] getActionsType() {
        int[] t = new int[actions.size()];
        for (int i = 0; i < actions.size(); i++) {
            t[i] = (actions.get(i).getActionType()).ordinal();
        }
        return t;
    }

    public int getDimension() {
        return this.dimension;
    }

    public String toString() {
        return "Dimensions: " + dimension + " " + actions.toString();
    }

}
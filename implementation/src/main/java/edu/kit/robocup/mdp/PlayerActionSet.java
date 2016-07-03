package edu.kit.robocup.mdp;

import java.io.Serializable;
import java.util.List;

import com.google.common.primitives.Doubles;
import edu.kit.robocup.game.PlayerAction;



public class PlayerActionSet implements Serializable {

    private final List<PlayerAction> actions;
    private final int dimension;
    private double[] array;

    public PlayerActionSet(List<PlayerAction> actions) {
        actions.sort((o1, o2) -> o1.getPlayerNumber() - o2.getPlayerNumber());
        this.actions = actions;
        int dim = 0;
        for (PlayerAction action: actions) {
            dim += action.getAction().getActionDimension();
        }
        this.dimension = dim;
        initArray();
    }

    public List<PlayerAction> getActions() {
        return this.actions;
    }

    public double[] getArray() {
        return array;
    }

    private void initArray() {
        array = new double[0];
        for (PlayerAction action : actions) {
            double[] act = action.getArray();
            array = Doubles.concat(array, act);
        }
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
        return actions.toString();
    } //"Dimensions: " + dimension + " " +

}

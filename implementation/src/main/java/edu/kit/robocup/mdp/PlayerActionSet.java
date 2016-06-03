package edu.kit.robocup.mdp;

import java.util.List;

import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.interf.mdp.IActionSet;

public class PlayerActionSet {

    private List<PlayerAction> actions;
    private int dimension;

    public PlayerActionSet(List<PlayerAction> actions) {
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

    public int getDimension() {
        return this.dimension;
    }

    public String toString() {
        return "Dimensions: " + dimension + " " + actions.toString();
    }

}

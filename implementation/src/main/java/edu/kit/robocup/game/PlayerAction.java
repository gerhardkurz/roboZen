package edu.kit.robocup.game;

import edu.kit.robocup.interf.game.IAction;

import java.io.Serializable;


public class PlayerAction implements Serializable {
    private int playerNumber;
    private IAction action;

    public PlayerAction(int playerNumber, IAction action) {
        this.playerNumber = playerNumber;
        this.action = action;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public IAction getAction() {
        return action;
    }

    public void setAction(IAction action) {
        this.action = action;
    }

    public double[] getArray() {
        return action.getArray();
    }

    public Action getActionType() {
        return action.getActionType();
    }

    @Override
    public String toString() {
        return "Pl: " + playerNumber + " act: " + action.toString();
    }
}

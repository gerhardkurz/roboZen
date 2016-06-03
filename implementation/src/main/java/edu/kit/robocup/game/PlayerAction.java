package edu.kit.robocup.game;

import edu.kit.robocup.interf.game.IAction;

import java.io.Serializable;


public class PlayerAction implements Serializable {


    private int playerNumber;
    private String teamName;
    private IAction action;

    public PlayerAction(int playerNumber, String teamName, IAction action) {
        this.playerNumber = playerNumber;
        this.teamName = teamName;
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

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public double[] getArray() {
        return action.getArray();
    }

    public Action getActionType() {
        return action.getActionType();
    }

    @Override
    public String toString() {
        return "Team: " + teamName + " Player: " + playerNumber + " action: " + action.toString();
    }
}

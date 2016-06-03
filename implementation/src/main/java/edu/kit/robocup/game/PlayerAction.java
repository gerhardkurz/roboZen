package edu.kit.robocup.game;

import edu.kit.robocup.interf.game.IAction;

import java.io.Serializable;


public class PlayerAction implements Serializable {
    int playerNumber;
    String teamName;
    IAction action;

    public PlayerAction(int playerNumber, String teamName, IAction action) {
        this.playerNumber = playerNumber;
        this.teamName = teamName;
        this.action = action;
    }

    @Override
    public String toString() {
        return "Team: " + teamName + " Player: " + playerNumber + " action: " + action.toString();
    }
}

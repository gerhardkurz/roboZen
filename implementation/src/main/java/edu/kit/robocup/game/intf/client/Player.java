package edu.kit.robocup.game.intf.client;


import edu.kit.robocup.game.intf.input.PlayerInput;
import edu.kit.robocup.game.intf.output.PlayerOutput;

public class Player extends StaffBase {
    private final PlayerInput input;
    private final PlayerOutput output;

    private boolean isTeamEast;
    private int number;

    public Player(String teamName, int number) {
        super(teamName);
        this.number = number;
        input = new PlayerInput(this);
        output = new PlayerOutput(this, teamName);
    }

    public PlayerInput getInput() {
        return input;
    }

    public PlayerOutput getOutput() {
        return output;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

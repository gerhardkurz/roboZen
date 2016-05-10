package edu.kit.robocup.game.action;


public class Dash extends Action {
    private final int power;

    public Dash(int power) {
        this.power = power;
    }

    @Override
    public String getCommandString() {
        commandFactory.addDashCommand(power);
        return commandFactory.next();

    }
}

package edu.kit.robocup.game.action;


public class Kick extends Action {
    private final int power;
    private final int direction;

    public Kick(int power, int direction) {
        this.power = power;
        this.direction = direction;
    }

    @Override
    public String getCommandString() {
        commandFactory.addKickCommand(power, direction);
        return commandFactory.next();

    }
}

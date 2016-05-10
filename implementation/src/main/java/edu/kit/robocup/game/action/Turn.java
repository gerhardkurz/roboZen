package edu.kit.robocup.game.action;


public class Turn extends Action {
    private final int angle;

    public Turn(int angle) {
        this.angle = angle;
    }

    @Override
    public String getCommandString() {
        commandFactory.addTurnCommand(angle);
        return commandFactory.next();
    }
}
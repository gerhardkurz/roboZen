package edu.kit.robocup.game.action;

public class Move extends Action {
    private final int x;
    private final int y;

    public Move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String getCommandString() {
        commandFactory.addMoveCommand(x, y);
        return commandFactory.next();
    }
}
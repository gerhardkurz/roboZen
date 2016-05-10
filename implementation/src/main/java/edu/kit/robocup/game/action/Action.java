package edu.kit.robocup.game.action;

import edu.kit.robocup.game.intf.parser.CommandFactory;

public abstract class Action {
    protected CommandFactory commandFactory = new CommandFactory();
    public abstract String getCommandString();
}

package edu.kit.robocup.game.action;


import edu.kit.robocup.game.intf.parser.CommandFactory;

import java.util.Random;

public class Yolo implements IAction {

    @Override
    public String getCommandString() {
        CommandFactory commandFactory = new CommandFactory();
        if (yoloGen()) {
            commandFactory.addTurnCommand(10);
        } else {
            commandFactory.addDashCommand(20);
        }
        return commandFactory.next();
    }

    private boolean yoloGen() {
        Random rnd = new Random();
        return rnd.nextFloat() <= 0.25;
    }
}

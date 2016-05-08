package edu.kit.robocup.game.intf.parser;


import org.apache.log4j.Logger;

public class Parser {
    private static final Logger logger = Logger.getLogger(Parser.class);

    private Parser() {}

    public static void parse(IInput input, String msg) {
        if (msg.startsWith("(ok look ")) {
            handleLook(input, msg);
        }
    }

    private static void handleLook(IInput input, String msg) {
        if (ICoachInput.class.isAssignableFrom(input.getClass())) {
            ICoachInput coachInput = (ICoachInput) input;
            coachInput.look(LookParser.parse(msg));
        }
    }


}

package edu.kit.robocup.game.server.message;


import edu.kit.robocup.game.Coach;
import org.apache.log4j.Logger;

public class Parser {
    private static final Logger logger = Logger.getLogger(Parser.class);

    private Parser() {}

    public static void parse(Object handler, String msg) {
        if (msg.startsWith("(see_global ")) {
            handleLook(handler, msg);
        }
    }

    private static void handleLook(Object handler, String msg) {
        if (Coach.class.isAssignableFrom(handler.getClass())) {
            Coach coach = (Coach) handler;
            coach.look(LookParser.parse(msg));
        }
    }


}

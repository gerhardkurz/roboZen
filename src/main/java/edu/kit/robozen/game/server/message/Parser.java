package edu.kit.robozen.game.server.message;


import edu.kit.robozen.game.PlayMode;
import edu.kit.robozen.game.controller.Coach;
import org.apache.log4j.Logger;

public class Parser {
    private static final Logger logger = Logger.getLogger(Parser.class);

    private Parser() {}

    public static void parse(Object handler, String msg) {
        if (Thread.currentThread().getName().endsWith("Coach")) {
            if (msg.startsWith("(init ")) {
                handleInit(handler);
            } else if (msg.startsWith("(see_global ")) {
                handleLook(handler, msg);
            } else if (msg.startsWith("(hear ") && msg.split(" ")[2].equals("referee")) {
                handleHear(handler, msg);
            }
        }
    }

    private static void handleInit(Object handler) {
        if (Coach.class.isAssignableFrom(handler.getClass())) {
            Coach coach = (Coach) handler;
            coach.init();
        }
    }

    private static void handleLook(Object handler, String msg) {
        if (Coach.class.isAssignableFrom(handler.getClass())) {
            Coach coach = (Coach) handler;
            coach.look(LookParser.parse(msg));
        }
    }

    private static void handleHear(Object handler, String msg) {
        String[] messageParts = msg.split(" ");
        if (Coach.class.isAssignableFrom(handler.getClass())) {
            Coach coach = (Coach) handler;
            coach.hear(PlayMode.get(messageParts[3]));
        }
    }
}

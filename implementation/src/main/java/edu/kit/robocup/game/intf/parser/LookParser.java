package edu.kit.robocup.game.intf.parser;


import edu.kit.robocup.game.State;

public class LookParser {

    private LookParser() {}

    public static State parse(String msg) {
        System.out.println(msg); // SEITE 101 manual
        return new State(null, null);
    }
}

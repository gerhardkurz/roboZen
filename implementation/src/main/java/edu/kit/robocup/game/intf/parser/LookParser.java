package edu.kit.robocup.game.intf.parser;


import edu.kit.robocup.game.BallState;
import edu.kit.robocup.game.PlayerState;
import edu.kit.robocup.game.State;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.*;

public class LookParser {

    private LookParser() {}

    public static State parse(String msg) {
        // SEITE 101 manual
        State state = null;
        Pattern re_seeGlobal = Pattern.compile("\\((?<command>see_global) (?<time>\\d+) (?<info>.*)\\)");
        Matcher match = re_seeGlobal.matcher(msg);

        if (match.find()) {
            String infoStr = match.group("info");
            state = parseInfo(infoStr);
        } else {
            System.out.println("NO MATCH");
        }
        return state;
}

    public static State parseInfo(String info) {

        List<PlayerState> players = new LinkedList<>();
        BallState ball = null;
        //String re_double = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
        Pattern re_objAndDesc = Pattern.compile("(\\((?<type>b|(p \"(?<teamname>.*?)\" (?<playerNr>\\d+)))\\) (?<first>[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?) (?<second>[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?) (?<third>[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?) (?<fourth>[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?)( (?<fifth>[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?) (?<sixth>[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?))?( (?<seventh>[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?))?)");
        Matcher match = re_objAndDesc.matcher(info);
        while (match.find()) {
            String type = match.group("type");
            double posX = Double.parseDouble(match.group("first"));
            double posY = Double.parseDouble(match.group("second"));
            double velX = Double.parseDouble(match.group("third"));
            double velY = Double.parseDouble(match.group("fourth"));
            if (type.equals("b")) {
                ball = new BallState(posX, posY, velX, velY);
            } else if (type.startsWith("p ")) {
                String teamname = match.group("teamname");
                int playerNr = Integer.parseInt(match.group("playerNr"));
                double bodyAngle = Double.parseDouble(match.group("fifth"));
                //double neckAngle = Double.parseDouble(match.group("sixth"));
                //double pointingDir = Double.parseDouble(match.group("seventh"));
                PlayerState player = new PlayerState(teamname, playerNr, posX, posY, velX, velY, bodyAngle);
                players.add(player);
            } else {
                // unknown match. May be ok.
            }
        }
        if (ball == null || players.isEmpty()) {
            System.out.println("Parsing seems to be failed");
        }
        return new State(ball, players);
    }
}

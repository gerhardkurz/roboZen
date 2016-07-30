package edu.kit.robozen.game.server.message;


import edu.kit.robozen.constant.PitchSide;
import edu.kit.robozen.game.state.Ball;
import edu.kit.robozen.interf.game.IPlayerState;
import edu.kit.robozen.game.state.PlayerState;
import edu.kit.robozen.game.state.State;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.*;

public class LookParser {

    private LookParser() {}

    public static State parse(String msg) {
        Pattern re_seeGlobal = Pattern.compile("\\((?<command>see_global) (?<time>\\d+) (?<info>.*)\\)");
        Matcher match = re_seeGlobal.matcher(msg);
        State state = null;

        if (match.find()) {
            String infoStr = match.group("info");
            state = parseInfo(infoStr);
        } else {
            System.out.println("NO MATCH");
        }
        return state;
}

    private static State parseInfo(String info) {

        List<IPlayerState> players = new LinkedList<>();
        Ball ball = null;
        //String re_double = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
        Pattern re_objAndDesc = Pattern.compile("(\\((?<type>b|(p \"(?<teamname>.*?)\" (?<playerNr>\\d+)))\\) (?<first>[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?) (?<second>[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?) (?<third>[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?) (?<fourth>[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?)( (?<fifth>[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?) (?<sixth>[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?))?( (?<seventh>[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?))?)");
        Matcher match = re_objAndDesc.matcher(info);
        while (match.find()) {
            String type = match.group("type");
            double positionX = Double.parseDouble(match.group("first"));
            double positionY = Double.parseDouble(match.group("second"));
            double velocityX = Double.parseDouble(match.group("third"));
            double velocityY = Double.parseDouble(match.group("fourth"));
            if (type.equals("b")) {
                ball = new Ball(positionX, positionY, velocityX, velocityY);
            } else if (type.startsWith("p ")) {
                String teamName = match.group("teamname");
                int number = Integer.parseInt(match.group("playerNr"));
                double bodyAngle = Double.parseDouble(match.group("fifth"));
                double neckAngle = Double.parseDouble(match.group("sixth"));
                //double pointingDir = Double.parseDouble(match.group("seventh"));

                PlayerState player = new PlayerState(PitchSide.valueOf(teamName), number, positionX, positionY, velocityX, velocityY, bodyAngle, neckAngle);
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

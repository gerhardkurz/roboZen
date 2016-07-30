package edu.kit.robozen.example;

import edu.kit.robozen.game.PlayMode;
import edu.kit.robozen.game.state.Ball;
import edu.kit.robozen.example.policy.ChaseAndKickPolicy;
import edu.kit.robozen.example.policy.PerPlayModePolicy;
import edu.kit.robozen.example.policy.heurisic.BeforeGamePolicy;
import edu.kit.robozen.example.policy.heurisic.KickOffPolicy;
import edu.kit.robozen.util.Util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class BasicMainWin {

    static Logger logger = Logger.getLogger(BasicMainWin.class.getName());


    public static void main(String[] args) throws IOException, InterruptedException {
        PerPlayModePolicy perPlayModePolicy = new PerPlayModePolicy(new ChaseAndKickPolicy());
        perPlayModePolicy.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        perPlayModePolicy.replacePolicyForPlayMode(new BeforeGamePolicy(), PlayMode.UNKNOWN);

        Util.TeamDescription teamDescription = new Util.TeamDescription(perPlayModePolicy, 5, new ArrayList<>());
        Util.executeGameWin("example_working_dir", teamDescription, teamDescription, t -> t.moveBall(new Ball(0, 0)));
    }
}

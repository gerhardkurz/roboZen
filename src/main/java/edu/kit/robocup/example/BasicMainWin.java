package edu.kit.robocup.example;

import edu.kit.robocup.game.PlayMode;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.example.policy.ChaseAndKickPolicy;
import edu.kit.robocup.example.policy.PerPlayModePolicy;
import edu.kit.robocup.example.policy.heurisic.BeforeGamePolicy;
import edu.kit.robocup.example.policy.heurisic.KickOffPolicy;
import edu.kit.robocup.util.Util;

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

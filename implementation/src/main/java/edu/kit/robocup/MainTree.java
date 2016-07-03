package edu.kit.robocup;

import edu.kit.robocup.game.PlayMode;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.mdp.policy.ChaseAndKickPolicy;
import edu.kit.robocup.mdp.policy.PerPlayModePolicy;
import edu.kit.robocup.mdp.policy.heurisic.BeforeGamePolicy;
import edu.kit.robocup.mdp.policy.heurisic.KickOffPolicy;
import edu.kit.robocup.tree.TreePolicy;
import edu.kit.robocup.util.Util;

import java.util.ArrayList;


public class MainTree {
    public static void main(String[] args) throws InterruptedException {
        PerPlayModePolicy perPlayModePolicy = new PerPlayModePolicy(new TreePolicy());
        perPlayModePolicy.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        perPlayModePolicy.replacePolicyForPlayMode(new BeforeGamePolicy(), PlayMode.UNKNOWN);

        PerPlayModePolicy perPlayModePolicy1 = new PerPlayModePolicy(new ChaseAndKickPolicy());
        perPlayModePolicy1.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        perPlayModePolicy1.replacePolicyForPlayMode(new BeforeGamePolicy(), PlayMode.UNKNOWN);


        Util.TeamDescription teamDescription = new Util.TeamDescription(perPlayModePolicy, 2, new ArrayList<>());
        Util.TeamDescription teamDescription1 = new Util.TeamDescription(perPlayModePolicy1, 2, new ArrayList<>());
        Util.executeGame(teamDescription, teamDescription1, new Ball(0, 0));
    }
}

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
        PerPlayModePolicy policyWest = new PerPlayModePolicy(new TreePolicy());
        policyWest.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        policyWest.replacePolicyForPlayMode(new BeforeGamePolicy(), PlayMode.UNKNOWN);

        PerPlayModePolicy policyEast = new PerPlayModePolicy(new ChaseAndKickPolicy());
        policyEast.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        policyEast.replacePolicyForPlayMode(new BeforeGamePolicy(), PlayMode.UNKNOWN);


        Util.TeamDescription teamWest = new Util.TeamDescription(policyWest, 2, new ArrayList<>());
        Util.TeamDescription teamEast = new Util.TeamDescription(policyEast, 2, new ArrayList<>());
        Util.executeGame(teamWest, teamEast, t -> t.moveBall(new Ball(0, 0)));
    }
}

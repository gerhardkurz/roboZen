package edu.kit.robocup;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.PlayMode;
import edu.kit.robocup.game.controller.Team;
import edu.kit.robocup.game.controller.Trainer;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.mdp.policy.ChaseAndKickPolicy;
import edu.kit.robocup.util.Util;
import edu.kit.robocup.mdp.policy.DoNothing;
import edu.kit.robocup.mdp.policy.PerPlayModePolicy;
import edu.kit.robocup.mdp.policy.heurisic.BeforeGamePolicy;
import edu.kit.robocup.mdp.policy.heurisic.KickOffPolicy;
import edu.kit.robocup.tree.TreePolicy;

/**
 * Created by dani on 27.06.2016.
 */
public class MainTreeShootGoal {


    public static void main(String[] args) throws InterruptedException {
        PerPlayModePolicy perPlayModePolicy = new PerPlayModePolicy(new TreePolicy());
        perPlayModePolicy.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        perPlayModePolicy.replacePolicyForPlayMode(new BeforeGamePolicy(), PlayMode.UNKNOWN);

        PerPlayModePolicy perPlayModePolicy1 = new PerPlayModePolicy(new DoNothing());//new ChaseAndKickPolicy());
        perPlayModePolicy1.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        perPlayModePolicy1.replacePolicyForPlayMode(new BeforeGamePolicy(), PlayMode.UNKNOWN);

        Util.initEnvironment();

        Trainer trainer = new Trainer("Trainer");
        trainer.connect();
        Team team1 = new Team(PitchSide.EAST, 2, perPlayModePolicy);
        team1.connectAll();

        Team team2 = new Team(PitchSide.WEST, 2, perPlayModePolicy1);
        team2.connectAll();

        trainer.movePlayer(new PlayerState(PitchSide.WEST, 1, -5, 5));
        trainer.movePlayer(new PlayerState(PitchSide.WEST, 2, -5, -5));

        trainer.movePlayer(new PlayerState(PitchSide.EAST, 1, -5, -5));
        trainer.movePlayer(new PlayerState(PitchSide.EAST, 2, -5, 5));

        Thread.sleep(1000);
        trainer.moveBall(new Ball(0, 0));
        trainer.changePlayMode(com.github.robocup_atan.atan.model.enums.PlayMode.PLAY_ON);
    }
}

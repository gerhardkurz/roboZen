package edu.kit.robocup;


import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.PlayMode;
import edu.kit.robocup.game.controller.Team;
import edu.kit.robocup.game.controller.Trainer;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.mdp.policy.ChaseAndKickPolicy;
import edu.kit.robocup.mdp.policy.DoNothing;
import edu.kit.robocup.mdp.policy.PerPlayModePolicy;
import edu.kit.robocup.mdp.policy.heurisic.BeforeGamePolicy;
import edu.kit.robocup.mdp.policy.heurisic.KickOffPolicy;

public class MainHeuristic {
    public static void main(String[] args) throws InterruptedException {
        Util.initEnvironment();

        Trainer trainer = new Trainer("Trainer");
        trainer.connect();

        PerPlayModePolicy perPlayModePolicy = new PerPlayModePolicy(new ChaseAndKickPolicy());
        perPlayModePolicy.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        perPlayModePolicy.replacePolicyForPlayMode(new BeforeGamePolicy(), PlayMode.UNKNOWN);
        Team team1 = new Team(PitchSide.WEST, 5, perPlayModePolicy);
        team1.connectAll();

        Team team2 = new Team(PitchSide.EAST, 5, perPlayModePolicy);
        team2.connectAll();

//        trainer.movePlayer(new PlayerState(PitchSide.WEST, 1, -5, 5));
//        trainer.movePlayer(new PlayerState(PitchSide.WEST, 2, -5, -5));
//        trainer.movePlayer(new PlayerState(PitchSide.WEST, 3, -5, 25));
//        trainer.movePlayer(new PlayerState(PitchSide.WEST, 4, -5, -25));
//        trainer.movePlayer(new PlayerState(PitchSide.WEST, 5, -5, 25));
//
//        trainer.movePlayer(new PlayerState(PitchSide.EAST, 1, 0, 0));
//        trainer.movePlayer(new PlayerState(PitchSide.EAST, 2, 5, 5));
//        trainer.movePlayer(new PlayerState(PitchSide.EAST, 3, 0, 0));
//        trainer.movePlayer(new PlayerState(PitchSide.EAST, 4, 5, -25));
//        trainer.movePlayer(new PlayerState(PitchSide.EAST, 5, 0, 0));

        Thread.sleep(200);
        trainer.changePlayMode(com.github.robocup_atan.atan.model.enums.PlayMode.PLAY_ON);

    }
}

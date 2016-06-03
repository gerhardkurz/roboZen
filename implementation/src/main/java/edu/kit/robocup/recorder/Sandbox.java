package edu.kit.robocup.recorder;


import java.io.*;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.PlayMode;
import edu.kit.robocup.game.controller.Team;
import edu.kit.robocup.game.controller.Trainer;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.mdp.policy.ChaseAndKickPolicy;
import edu.kit.robocup.mdp.policy.PerPlayModePolicy;
import edu.kit.robocup.mdp.policy.heurisic.KickOffPolicy;
import org.apache.log4j.Logger;

import static edu.kit.robocup.Main.initEnvironment;


public class Sandbox {

    static Logger logger = Logger.getLogger(Sandbox.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {

        initEnvironment();

        Trainer trainer = new Trainer("Trainer");
        trainer.connect();



        PerPlayModePolicy perPlayModePolicy = new PerPlayModePolicy(new ChaseAndKickPolicy());
        perPlayModePolicy.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST);
        GameRecorder recordingPolicy = new GameRecorder("test", perPlayModePolicy);

        Team team1 = new Team(PitchSide.WEST, 2, recordingPolicy);
        team1.connectAll();

        Team team2 = new Team(PitchSide.EAST, 2, recordingPolicy);
        team2.connectAll();

        trainer.movePlayer(new PlayerState(PitchSide.WEST, 1, -5, 25));
        trainer.movePlayer(new PlayerState(PitchSide.WEST, 2, -5, -25));
        trainer.movePlayer(new PlayerState(PitchSide.EAST, 1, 0, 0));
        trainer.movePlayer(new PlayerState(PitchSide.EAST, 2, 5, -25));

        Thread.sleep(100);
        trainer.moveBall(new Ball(0, 0));
        trainer.changePlayMode(com.github.robocup_atan.atan.model.enums.PlayMode.PLAY_ON);
    }
}

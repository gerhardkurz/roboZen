package edu.kit.robocup;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.PlayMode;
import edu.kit.robocup.game.controller.Team;
import edu.kit.robocup.game.controller.Trainer;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.mdp.policy.AllActionCombinationsPolicy;
import edu.kit.robocup.mdp.policy.ChaseAndKickPolicy;
import edu.kit.robocup.mdp.policy.PerPlayModePolicy;
import edu.kit.robocup.mdp.policy.heurisic.KickOffPolicy;
import edu.kit.robocup.recorder.GameRecorder;
import org.apache.log4j.Logger;

import java.io.IOException;


public class MainRecordings {

    static Logger logger = Logger.getLogger(MainRecordings.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        PerPlayModePolicy policy1 = new PerPlayModePolicy(new GameRecorder("recordings/random300", new AllActionCombinationsPolicy()));
        policy1.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);

        IPolicy[] policies = {policy1};
        Util.initEnvironment();

        Trainer trainer = new Trainer("Trainer");
        trainer.connect();

        PerPlayModePolicy perPlayModePolicy = new PerPlayModePolicy(new GameRecorder("recordings/random300", new AllActionCombinationsPolicy()));
        perPlayModePolicy.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        Team team1 = new Team(PitchSide.WEST, 2, new ChaseAndKickPolicy());
        team1.connectAll();

        Team team2 = new Team(PitchSide.EAST, 2, perPlayModePolicy);
        team2.connectAll();

        trainer.movePlayer(new PlayerState(PitchSide.WEST, 1, -30, 60));
        trainer.movePlayer(new PlayerState(PitchSide.WEST, 2, -30, -30));

        trainer.movePlayer(new PlayerState(PitchSide.EAST, 1, 10, 10));
        trainer.movePlayer(new PlayerState(PitchSide.EAST, 2, 10, 10));

        Thread.sleep(100);
        trainer.moveBall(new Ball(10, 10));
        trainer.changePlayMode(com.github.robocup_atan.atan.model.enums.PlayMode.PLAY_ON);
        while (trainer.getInitMessage().toString() == "") { // TODO Spielstand != 0:0
            Thread.sleep(1);
        }
        Util.killTask("rcssserver.exe");
        Util.killTask("rcssmonitor.exe");

        Util.initEnvironment();

        trainer = new Trainer("Trainer");
        trainer.connect();

        perPlayModePolicy = new PerPlayModePolicy(new GameRecorder("recordings/chaseAndKick300", new ChaseAndKickPolicy()));
        perPlayModePolicy.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        team1 = new Team(PitchSide.WEST, 2, perPlayModePolicy);
        team1.connectAll();

        team2 = new Team(PitchSide.EAST, 2, perPlayModePolicy);
        team2.connectAll();

        trainer.movePlayer(new PlayerState(PitchSide.WEST, 1, -5, 5));
        trainer.movePlayer(new PlayerState(PitchSide.WEST, 2, -5, -5));

        trainer.movePlayer(new PlayerState(PitchSide.EAST, 1, 5, 5));
        trainer.movePlayer(new PlayerState(PitchSide.EAST, 2, 5, -5));

        Thread.sleep(100);
        trainer.moveBall(new Ball(0, 0));
        trainer.changePlayMode(com.github.robocup_atan.atan.model.enums.PlayMode.PLAY_ON);
        while (trainer.getInitMessage().toString() == "") { // TODO Spielstand != 0:0
            Thread.sleep(1);
        }
        Util.killTask("rcssserver.exe");
        Util.killTask("rcssmonitor.exe");

        Util.initEnvironment();

        trainer = new Trainer("Trainer");
        trainer.connect();

        perPlayModePolicy = new PerPlayModePolicy(new GameRecorder("recordings/chaseAndKick3001", new ChaseAndKickPolicy()));
        perPlayModePolicy.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        team1 = new Team(PitchSide.WEST, 2, perPlayModePolicy);
        team1.connectAll();

        team2 = new Team(PitchSide.EAST, 2, perPlayModePolicy);
        team2.connectAll();

        trainer.movePlayer(new PlayerState(PitchSide.WEST, 1, -5, 5));
        trainer.movePlayer(new PlayerState(PitchSide.WEST, 2, -5, -5));

        trainer.movePlayer(new PlayerState(PitchSide.EAST, 1, 50, 50));
        trainer.movePlayer(new PlayerState(PitchSide.EAST, 2, 50, -50));

        Thread.sleep(100);
        trainer.moveBall(new Ball(0, 0));
        trainer.changePlayMode(com.github.robocup_atan.atan.model.enums.PlayMode.PLAY_ON);
        while (trainer.getInitMessage().toString() == "") { // TODO Spielstand != 0:0
            Thread.sleep(1);
        }
        Util.killTask("rcssserver.exe");
        Util.killTask("rcssmonitor.exe");

        Util.initEnvironment();

        trainer = new Trainer("Trainer");
        trainer.connect();

        perPlayModePolicy = new PerPlayModePolicy(new GameRecorder("recordings/chaseAndKick3002", new ChaseAndKickPolicy()));
        perPlayModePolicy.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        team1 = new Team(PitchSide.WEST, 2, perPlayModePolicy);
        team1.connectAll();

        team2 = new Team(PitchSide.EAST, 2, perPlayModePolicy);
        team2.connectAll();

        trainer.movePlayer(new PlayerState(PitchSide.WEST, 1, -50, 50));
        trainer.movePlayer(new PlayerState(PitchSide.WEST, 2, -50, -50));

        trainer.movePlayer(new PlayerState(PitchSide.EAST, 1, 5, 5));
        trainer.movePlayer(new PlayerState(PitchSide.EAST, 2, 5, -5));

        Thread.sleep(100);
        trainer.moveBall(new Ball(0, 0));
        trainer.changePlayMode(com.github.robocup_atan.atan.model.enums.PlayMode.PLAY_ON);
        while (trainer.getInitMessage().toString() == "") { // TODO Spielstand != 0:0
            Thread.sleep(1);
        }
        Util.killTask("rcssserver.exe");
        Util.killTask("rcssmonitor.exe");
    }
}

package edu.kit.robocup;

import com.github.robocup_atan.atan.model.enums.PlayMode;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.game.controller.Team;
import edu.kit.robocup.game.controller.Trainer;
import edu.kit.robocup.mdp.policy.ChaseAndKickPolicy;
import edu.kit.robocup.mdp.policy.RandomPolicy;
import edu.kit.robocup.recorder.GameRecorder;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;

import org.apache.log4j.Logger;

public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());




    public static void main(String[] args) throws IOException, InterruptedException {
//        getAngleBetween(-1, -1, 0, 1);
//        System.exit(0);
        initEnvironment();

        Trainer trainer = new Trainer("Trainer");
        trainer.connect();

        Team team1 = new Team("t1", 2, new ChaseAndKickPolicy());
        team1.connectAll();

        Team team2 = new Team("t2", 2, new ChaseAndKickPolicy());
        team2.connectAll();

        trainer.movePlayer(new PlayerState("t1", 1, 0, 0));
        trainer.movePlayer(new PlayerState("t1", 2, -20, -20));
        trainer.movePlayer(new PlayerState("t2", 1, 0, 20));
        trainer.movePlayer(new PlayerState("t2", 2, 20, 0));

        Thread.sleep(100);
        trainer.moveBall(new Ball(10, 10));
        trainer.changePlayMode(PlayMode.PLAY_ON);
        team1.getCoach().eye(true);
        team2.getCoach().eye(true);
    }

    private static void initEnvironment() {
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        Util.startServer();
        Util.startMonitor();
    }
}

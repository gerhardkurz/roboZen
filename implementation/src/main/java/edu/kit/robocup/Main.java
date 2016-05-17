package edu.kit.robocup;

import com.github.robocup_atan.atan.model.enums.PlayMode;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.game.controller.Team;
import edu.kit.robocup.game.controller.Trainer;
import edu.kit.robocup.mdp.policys.ChaseAndKickPolicy;
import edu.kit.robocup.mdp.policys.RandomPolicy;
import edu.kit.robocup.recorder.GameRecorder;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;

import org.apache.log4j.Logger;

public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());
    
    public static void main(String[] args) throws IOException {
        initEnvironment();

        Trainer trainer = new Trainer("Trainer");
        trainer.connect();

        Team team1 = new Team("t1", 2, new GameRecorder("test", new ChaseAndKickPolicy()));
        team1.connectAll();

        Team team2 = new Team("t2", 2, new RandomPolicy());
        team2.connectAll();

        trainer.movePlayer(new PlayerState("t1", 1, 1, 10));
        trainer.movePlayer(new PlayerState("t1", 2, 1, 5));
        trainer.movePlayer(new PlayerState("t2", 1, 1, -5));
        trainer.movePlayer(new PlayerState("t2", 2, 1, -10));

        trainer.moveBall(new Ball(13, 13));
        trainer.changePlayMode(PlayMode.KICK_OFF_L);

        team1.getCoach().eye(true); // enables constant visual updates for trainer/coach
        team2.getCoach().eye(true);
    }

    private static void initEnvironment() {
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        Util.startServer();
        Util.startMonitor();
    }
}

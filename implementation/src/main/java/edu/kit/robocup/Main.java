package edu.kit.robocup;

import com.github.robocup_atan.atan.model.enums.PlayMode;
import edu.kit.robocup.game.Ball;
import edu.kit.robocup.game.PlayerState;
import edu.kit.robocup.game.Team;
import edu.kit.robocup.game.Trainer;
import edu.kit.robocup.mdp.DummyPolicy;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;

import org.apache.log4j.Logger;

public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());


    public static void main(String[] args) throws IOException {
        initEnvironment();

        Trainer trainer = new Trainer("Trainer");
        trainer.connect();

        Team team1 = new Team("t1", 1, new DummyPolicy("t1"));
        team1.connectAll();

        Team team2 = new Team("t2", 2, new DummyPolicy("t2"));
        team2.connectAll();

        trainer.movePlayer(new PlayerState("t1", 1, 20, 20));
        trainer.movePlayer(new PlayerState("t2", 1, 20, 20));
        trainer.movePlayer(new PlayerState("t2", 2, 20, -20));

        trainer.moveBall(new Ball(3, 3));
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

package edu.kit.robocup;

import com.github.robocup_atan.atan.model.enums.PlayMode;
import edu.kit.robocup.game.intf.client.Team;
import edu.kit.robocup.game.intf.client.Trainer;
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

        Team team1 = new Team("t1", 1, new DummyPolicy());
        team1.connectAll();

        Team team2 = new Team("t2", 2, new DummyPolicy());
        team2.connectAll();

        trainer.movePlayer(team1.getPlayerOutput(0), -20, 20);
        trainer.movePlayer(team2.getPlayerOutput(0), 20, 20);
        trainer.movePlayer(team2.getPlayerOutput(1), 20, -20);

        trainer.moveBall(5, 5);
        trainer.changePlayMode(PlayMode.KICK_OFF_L);
        team1.getCoach().look();
        team2.getCoach().look();

        team1.getCoach().receiveGlobalVisInfo();
    }

    private static void initEnvironment() {
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        Util.startServer();
        Util.startMonitor();
    }
}

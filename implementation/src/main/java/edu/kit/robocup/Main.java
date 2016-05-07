package edu.kit.robocup;

import com.github.robocup_atan.atan.model.enums.PlayMode;
import edu.kit.robocup.game.intf.Team;
import edu.kit.robocup.game.intf.Trainer;
import edu.kit.robocup.game.intf.TrainerOutput;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;

import org.apache.log4j.Logger;

public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());


    public static void main(String[] args) throws IOException {
        initEnvironment();

        Trainer trainer = new Trainer("Trainer");
        TrainerOutput trainerOutput = trainer.getOutput();
        trainerOutput.connect();

        Team team1 = new Team("t1", 1);
        team1.connectAll();

        Team team2 = new Team("t2", 1);
        team2.connectAll();

        trainerOutput.movePlayer(team1.getPlayerOutput(0), -20, 20);
        trainerOutput.movePlayer(team2.getPlayerOutput(0), 20, 20);
        trainerOutput.changePlayMode(PlayMode.PLAY_ON);
    }

    private static void initEnvironment() {
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        Util.startServer();
        Util.startMonitor();
    }
}

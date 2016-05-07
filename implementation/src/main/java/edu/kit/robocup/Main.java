package edu.kit.robocup;

import edu.kit.robocup.game.connection.Team;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;

import org.apache.log4j.Logger;

public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());


    public static void main(String[] args) throws IOException {
        initEnvironment();


        Team team1 = new Team("t1", 1);
        team1.connectAll();

        Team team2 = new Team("t2", 1);
        team2.connectAll();

        team1.getTrainerOutput().movePlayer(team1.getPlayerOutput(0), 50, 50);
    }

    private static void initEnvironment() {
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        Util.startServer();
        Util.startMonitor();
    }
}

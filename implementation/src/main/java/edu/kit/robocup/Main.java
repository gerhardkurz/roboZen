package edu.kit.robocup;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.PlayMode;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.game.controller.Team;
import edu.kit.robocup.game.controller.Trainer;
import edu.kit.robocup.mdp.policy.*;
import edu.kit.robocup.mdp.policy.heurisic.KickOffPolicy;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());


    public static void main(String[] args) throws IOException, InterruptedException {
//        getAngleBetween(-1, -1, 0, 1);
////        System.exit(0);
//        List<Game> games = new ArrayList<Game>();
//        GameReader r = new GameReader("allActionCombinationsReduced50");
//        //games.add(r.getGameFromFile());
//        //r = new GameReader("ChaseAndKickReduced50");
//        //games.add(r.getGameFromFile());
//        //r = new GameReader("ChaseAndKickReducedWithoutGoal1");
//        games.add(r.getGameFromFile());
//        //Transitions t = new Transitions(games);
//        //t.startLearning();
//        ValueIteration v = new ValueIteration(games, new Reward(200, -200, 50, -50, 70, 170, -170, false, "t1"));
//
//        IPolicy valueiterationPolicy = v.solve();

        initEnvironment();

        Trainer trainer = new Trainer("Trainer");
        trainer.connect();
        PerPlayModePolicy perPlayModePolicy = new PerPlayModePolicy(new ChaseAndKickPolicy());
        perPlayModePolicy.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST);
        Team team1 = new Team(PitchSide.WEST, 2, perPlayModePolicy);
        team1.connectAll();

        Team team2 = new Team(PitchSide.EAST, 2, perPlayModePolicy);
        team2.connectAll();

        trainer.movePlayer(new PlayerState(PitchSide.WEST, 1, -5, 25));
        trainer.movePlayer(new PlayerState(PitchSide.WEST, 2, -5, -25));
        trainer.movePlayer(new PlayerState(PitchSide.EAST, 1, 0, 0));
        trainer.movePlayer(new PlayerState(PitchSide.EAST, 2, 5, -25));

        Thread.sleep(100);
        trainer.moveBall(new Ball(0, 0));
        trainer.changePlayMode(com.github.robocup_atan.atan.model.enums.PlayMode.PLAY_ON);

    }

    private static void initEnvironment() {
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        Util.startServer();
        Util.startMonitor();
    }
}

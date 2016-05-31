package edu.kit.robocup;

import com.github.robocup_atan.atan.model.enums.PlayMode;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.game.controller.Team;
import edu.kit.robocup.game.controller.Trainer;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.mdp.Reward;
import edu.kit.robocup.mdp.ValueIteration;
import edu.kit.robocup.mdp.policy.*;
import edu.kit.robocup.mdp.transition.Game;
import edu.kit.robocup.mdp.transition.Transitions;
import edu.kit.robocup.recorder.GameReader;
import edu.kit.robocup.recorder.GameRecorder;
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

        Team team1 = new Team("t1", PitchSide.WEST, 2, new ChaseAndKickPolicy());
        team1.connectAll();

        Team team2 = new Team("t2", PitchSide.EAST, 2, new ChaseAndKickPolicy());
        team2.connectAll();

        trainer.movePlayer(new PlayerState("t1", 1, 0, 0));
        trainer.movePlayer(new PlayerState("t1", 2, -5, -5));
        trainer.movePlayer(new PlayerState("t2", 1, 5, 25));
        trainer.movePlayer(new PlayerState("t2", 2, 5, -25));

        Thread.sleep(100);
        trainer.moveBall(new Ball(0, 0));
        trainer.changePlayMode(PlayMode.PLAY_ON);

    }

    private static void initEnvironment() {
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        Util.startServer();
        Util.startMonitor();
    }
}

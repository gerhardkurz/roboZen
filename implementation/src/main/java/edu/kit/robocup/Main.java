package edu.kit.robocup;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.PlayMode;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.game.controller.Team;
import edu.kit.robocup.game.controller.Trainer;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.mdp.Reward;
import edu.kit.robocup.mdp.SimpleReward;
import edu.kit.robocup.mdp.ValueIteration;
import edu.kit.robocup.mdp.policy.*;
import edu.kit.robocup.mdp.policy.heurisic.KickOffPolicy;
import edu.kit.robocup.mdp.transition.Game;
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
//        System.exit(0);
        List<Game> games = new ArrayList<Game>();
        GameReader r = new GameReader("recordings/chaseAndKick3001");
        games.add(r.getGameFromFile());
        r = new GameReader("recordings/chaseAndKick300");
        games.add(r.getGameFromFile());
        r = new GameReader("recordings/random300");
        games.add(r.getGameFromFile());
        ValueIteration v = new ValueIteration(games, new Reward(2000,-2000,50, -50, 70, 170, -170, PitchSide.EAST));

        //double[] theta90Iterations = new double[]{-0.000963, 0.269859, -0.002512, 0.040671, -0.070394, -0.008944, -0.019962, -0.03694, -0.002041, -0.010458, 0.115703, 0.027337, -0.023096, -0.02907, 0.018174, -0.178805, -0.063191, 0.043526, -0.041956, -0.023118, -0.039069, 0.142336, -0.02465, 0.008617};
        //Transitions t = new Transitions(games);
        //t.startLearning();
        //DoubleFactory1D h = DoubleFactory1D.dense;
        //IPolicy valueiterationPolicy = new ValueIterationPolicy(h.make(theta90Iterations), new Reward(200,-200,50, -50, 70, 170, -170, false ,"t1"), t);

        IPolicy valueiterationPolicy = v.solve();

        /*initEnvironment();

        Trainer trainer = new Trainer("Trainer");
        trainer.connect();

        PerPlayModePolicy perPlayModePolicy = new PerPlayModePolicy(new AllActionCombinationsPolicy());
        perPlayModePolicy.replacePolicyForPlayMode(new DoNothing(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        Team team1 = new Team(PitchSide.WEST, 2, perPlayModePolicy);
        team1.connectAll();

        Team team2 = new Team(PitchSide.EAST, 2, new GameRecorder("recordings/random300", perPlayModePolicy));
        team2.connectAll();

        //trainer.movePlayer(new PlayerState(PitchSide.WEST, 1, -50, -50));
        //trainer.movePlayer(new PlayerState(PitchSide.WEST, 2, -23, -25));

        trainer.movePlayer(new PlayerState(PitchSide.WEST, 1, -5, 5));
        trainer.movePlayer(new PlayerState(PitchSide.WEST, 2, -5, -5));
        //trainer.movePlayer(new PlayerState(PitchSide.WEST, 3, -5, 25));
        //trainer.movePlayer(new PlayerState(PitchSide.WEST, 4, -5, -25));
        //trainer.movePlayer(new PlayerState(PitchSide.WEST, 5, -5, 25));

        //trainer.movePlayer(new PlayerState(PitchSide.EAST, 1, 3, 3));
        //trainer.movePlayer(new PlayerState(PitchSide.EAST, 2, 5, 10));

        trainer.movePlayer(new PlayerState(PitchSide.EAST, 1, 0, 0));
        trainer.movePlayer(new PlayerState(PitchSide.EAST, 2, 5, 5));
        //trainer.movePlayer(new PlayerState(PitchSide.EAST, 3, 0, 0));
        //trainer.movePlayer(new PlayerState(PitchSide.EAST, 4, 5, -25));
        //trainer.movePlayer(new PlayerState(PitchSide.EAST, 5, 0, 0));

        Thread.sleep(100);
        trainer.moveBall(new Ball(10, 10));
        trainer.changePlayMode(com.github.robocup_atan.atan.model.enums.PlayMode.PLAY_ON);*/

    }

    public static void initEnvironment() {
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        Util.startServer();
        Util.startMonitor();
    }
}

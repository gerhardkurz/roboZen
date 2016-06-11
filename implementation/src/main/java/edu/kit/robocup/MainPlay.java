package edu.kit.robocup;

import cern.colt.matrix.DoubleFactory1D;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.PlayMode;
import edu.kit.robocup.game.controller.Team;
import edu.kit.robocup.game.controller.Trainer;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.mdp.Reward;
import edu.kit.robocup.mdp.policy.ChaseAndKickPolicy;
import edu.kit.robocup.mdp.policy.PerPlayModePolicy;
import edu.kit.robocup.mdp.policy.ValueIterationPolicy;
import edu.kit.robocup.mdp.policy.heurisic.KickOffPolicy;
import edu.kit.robocup.mdp.transition.Game;
import edu.kit.robocup.mdp.transition.Transitions;
import edu.kit.robocup.recorder.GameReader;
import edu.kit.robocup.recorder.GameRecorder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainPlay {

    static Logger logger = Logger.getLogger(MainPlay.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        List<Game> games = new ArrayList<>();
        GameReader r = new GameReader("recordings/chaseAndKick300");
        games.add(r.getGameFromFile());
        r = new GameReader("recordings/chaseAndKick3001");
        games.add(r.getGameFromFile());
        r = new GameReader("recordings/chaseAndKick3002");
        games.add(r.getGameFromFile());
        r = new GameReader("recordings/random300");
        games.add(r.getGameFromFile());

        //double[] theta90Iterations = new double[]{-0.000963, 0.269859, -0.002512, 0.040671, -0.070394, -0.008944, -0.019962, -0.03694, -0.002041, -0.010458, 0.115703, 0.027337, -0.023096, -0.02907, 0.018174, -0.178805, -0.063191, 0.043526, -0.041956, -0.023118, -0.039069, 0.142336, -0.02465, 0.008617};
        double[] theta500Iterations = new double[]{-9.96505E+271, -3.862491E+271, -3.998974E+270, -2.245478E+269, -5.661369E+272, 1.104631E+272, -4.650037E+271, -1.973853E+271, 1.034372E+271, -2.088521E+272, -2.028838E+270, 3.259283E+272, 1.002286E+272, 2.796303E+271, 8.805387E+272, 1.324348E+272, 3.609485E+272, 3.206651E+271, -6.124972E+271, 1.084968E+272, 5.641088E+270, 1.412211E+271, 1.895076E+271, -8.958095E+270};

        Transitions t = new Transitions(games);
        t.setLearning("logall3.txt");
        DoubleFactory1D h = DoubleFactory1D.dense;
        IPolicy valueiterationPolicy = new ValueIterationPolicy(h.make(theta500Iterations), new Reward(2000,-2000,50, -50, 70, 170, -170, PitchSide.EAST), t);


        Util.initEnvironment();

        Trainer trainer = new Trainer("Trainer");
        trainer.connect();

        PerPlayModePolicy perPlayModePolicy = new PerPlayModePolicy(valueiterationPolicy);
        perPlayModePolicy.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        Team team1 = new Team(PitchSide.WEST, 2, new ChaseAndKickPolicy());
        team1.connectAll();

        Team team2 = new Team(PitchSide.EAST, 2, perPlayModePolicy);
        team2.connectAll();

        trainer.movePlayer(new PlayerState(PitchSide.WEST, 1, -5, 5));
        trainer.movePlayer(new PlayerState(PitchSide.WEST, 2, -5, -5));

        trainer.movePlayer(new PlayerState(PitchSide.EAST, 1, 10, 10));
        trainer.movePlayer(new PlayerState(PitchSide.EAST, 2, 10, 10));

        Thread.sleep(100);
        trainer.moveBall(new Ball(10, 10));
        trainer.changePlayMode(com.github.robocup_atan.atan.model.enums.PlayMode.PLAY_ON);
    }
}

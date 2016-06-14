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
import edu.kit.robocup.mdp.transition.ITransition;
import edu.kit.robocup.mdp.transition.Transition;
import edu.kit.robocup.mdp.transition.TransitionDet;
import edu.kit.robocup.recorder.GameReader;
import edu.kit.robocup.util.Util;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;


public class MainPlay {

    static Logger logger = Logger.getLogger(MainPlay.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        List<Game> games = GameReader.getGamesFromFiles("recordings/chaseAndKick", "recordings/chaseAndKick1", "recordings/chaseAndKick2", "recordings/random");

        double[] theta90Iterations = new double[]{-0.000963, 0.269859, -0.002512, 0.040671, -0.070394, -0.008944, -0.019962, -0.03694, -0.002041, -0.010458, 0.115703, 0.027337, -0.023096, -0.02907, 0.018174, -0.178805, -0.063191, 0.043526, -0.041956, -0.023118, -0.039069, 0.142336, -0.02465, 0.008617};
        double[] theta500Iterations = new double[]{-9.96505E+271, -3.862491E+271, -3.998974E+270, -2.245478E+269, -5.661369E+272, 1.104631E+272, -4.650037E+271, -1.973853E+271, 1.034372E+271, -2.088521E+272, -2.028838E+270, 3.259283E+272, 1.002286E+272, 2.796303E+271, 8.805387E+272, 1.324348E+272, 3.609485E+272, 3.206651E+271, -6.124972E+271, 1.084968E+272, 5.641088E+270, 1.412211E+271, 1.895076E+271, -8.958095E+270};
        double[] theta70Iterations10000Samples = new double[]{3.586892E+119, -3.745296E+120 ,-1.432131E+121, 9.05253E+119, 4.878049E+119, -1.207145E+120, 8.894006E+121, -8.673137E+118, -2.939898E+119, 1.483416E+119, 2.585866E+121, 4.420041E+119, -6.22495E+119, 4.042307E+118, 9.935912E+121, 3.105797E+119, 5.717366E+119, 1.58088E+120, 2.860505E+120, -4.850314E+120};
        double[] theta212iterations10000Samples = new double[]{-0.055668, 0.132674, 3.022018, -0.007703, -0.005145, 0.055861, -0.067511, -0.011228, -0.003991, -0.077248, 4.336487, -0.007263 ,0.066117 ,-0.054201 ,0.727672 ,-0.016712 ,-0.47223 ,0.020872 ,-9.294904 ,-0.796241};

        ITransition t = new TransitionDet(games.get(0).getNumberPlayers(), games.get(0).getNumberofAllPlayers(), games.get(0).getStates().get(0).getDimension());
        //t.setLearning("Transitions/save.txt");
        DoubleFactory1D h = DoubleFactory1D.dense;
        IPolicy valueiterationPolicy = new ValueIterationPolicy(h.make(theta212iterations10000Samples), new Reward(2000,-2000,50, -50, 70, 170, -170, PitchSide.EAST), t);


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

        trainer.movePlayer(new PlayerState(PitchSide.EAST, 1, 5, -5));
        trainer.movePlayer(new PlayerState(PitchSide.EAST, 2, 5, 5));

        Thread.sleep(100);
        trainer.moveBall(new Ball(-10, -10));
        trainer.changePlayMode(com.github.robocup_atan.atan.model.enums.PlayMode.PLAY_ON);
    }
}

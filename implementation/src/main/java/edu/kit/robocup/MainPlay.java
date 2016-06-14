package edu.kit.robocup;

import cern.colt.matrix.DoubleFactory1D;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.Dash;
import edu.kit.robocup.game.PlayMode;
import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.game.controller.Team;
import edu.kit.robocup.game.controller.Trainer;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.interf.mdp.IReward;
import edu.kit.robocup.interf.mdp.IState;
import edu.kit.robocup.mdp.PlayerActionSet;
import edu.kit.robocup.mdp.Reward;
import edu.kit.robocup.mdp.SimpleReward;
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
import java.util.ArrayList;
import java.util.List;


public class MainPlay {

    static Logger logger = Logger.getLogger(MainPlay.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        List<Game> games = GameReader.getGamesFromFiles("recordings/chaseAndKick", "recordings/chaseAndKick1", "recordings/chaseAndKick2", "recordings/random");

        double[] theta212iterations10000Samples07discount = new double[]{-0.055668, 0.132674, 3.022018, -0.007703, -0.005145, 0.055861, -0.067511, -0.011228, -0.003991, -0.077248, 4.336487, -0.007263 ,0.066117 ,-0.054201 ,0.727672 ,-0.016712 ,-0.47223 ,0.020872 ,-9.294904 ,-0.796241};
        double[] theta38Iterations10000SamplesConvergedSimpleReward07discount = new double[]{-0.017041 ,-0.013039, 1.445658 ,0.020225, 0.00768, 0.031494, -0.016159, 0.004823, 0.029029, 0.036609, -0.713965, -0.007466, 0.024826, -0.024973, 2.565945, 0.022212, -0.218072, 0.006131, -3.174722, -1.126635};
        double[] theta10000Samples09DiscountConverged = new double[]{-0.048416, -0.060917, 4.384677, -0.020362, -0.028074, -0.147261, 0.177449, 0.008627, -0.125131, -0.18913 ,7.408491, 0.030367 ,-0.031284 ,-0.047264, 3.206996 ,0.010512, -0.646374, 0.0826 ,-15.059248, 1.258918};
        double[] theta10000Samples09Discount14DiscreteActions = new double[]{0.09372, 0.095189, 2.922211, 0.017792, 0.071068 ,0.12762 ,1.471051, -0.007987, -0.015543, 0.047659, 2.114774, 0.015725, 0.05467, -0.060867, 5.169891, -0.005079, -0.49026 ,0.00334 ,-12.166075, 0.234946};
        double[] theta10000Samples09Discount14DiscretActionsConvergedSimpleReward = new double[]{0.073271, -0.148649, 0.611018, 0.020996, 0.136306, 0.038114, 3.342742, -0.008464, 0.126004 ,0.334871, 11.700119, 0.010679, -0.132036 ,-0.170976 ,4.758561 ,0.038954 ,-1.076235 ,-0.011755 ,-12.637459 ,-0.418387
        };

        ITransition t = new TransitionDet(games.get(0).getNumberPlayers(), games.get(0).getNumberofAllPlayers(), games.get(0).getStates().get(0).getDimension());
        //t.setLearning("Transitions/save.txt");
        DoubleFactory1D h = DoubleFactory1D.dense;
        IPolicy valueiterationPolicy = new ValueIterationPolicy(h.make(theta10000Samples09Discount14DiscretActionsConvergedSimpleReward), new Reward(2000,-2000,50, -50, 70, 170, -170, PitchSide.EAST), t);//new SimpleReward(2000, PitchSide.EAST), t);//
        /*List<IPlayerState> p = new ArrayList<IPlayerState>();
        p.add(new PlayerState(PitchSide.EAST, 1, 4.15109, -4.50557, 0.010239596606751656, -29.0, 0 ));
        p.add(new PlayerState(PitchSide.EAST, 2, 6.16164, 11.1434, 0.0039003053304011983, -115.0, 0));
        p.add(new PlayerState(PitchSide.WEST, 1, -18.9086, 12.9698, 0.27021949875795415, -34.0,0));
        p.add(new PlayerState(PitchSide.WEST, 2, -22.058, -19.3542, 0.07632880615927384,29.0,0));
        List<PlayerAction> l = new ArrayList<>();
        l.add(new PlayerAction(1, new Dash(40)));
        l.add(new PlayerAction(2, new Dash(40)));
        PlayerActionSet a = new PlayerActionSet(l);
        List<IPlayerState> q = new ArrayList<IPlayerState>();
        q.add(new PlayerState(PitchSide.EAST, 1, 4.15109, -4.50557, 0.010239596606751656 , -29.0,0));
        q.add(new PlayerState(PitchSide.EAST, 2, 6.16164, 11.1434, 0.0039003053304011983, -115.0, 0));
        q.add(new PlayerState(PitchSide.WEST, 1,-18.9086, 12.9698, 0.27021949875795415, -34.0 ,0));
        q.add(new PlayerState(PitchSide.WEST, 2, -22.058, -19.3542, 0.07632880615927384,29,0));
        IState sq = new State(new Ball(5, -5, 0, 0), q);
        IReward r = new Reward(2000,-2000,50, -50, 70, 170, -170, PitchSide.EAST);
        logger.info(r.calculateReward(new State(new Ball(5, -5, 0, 0), p), a, (State)sq) + h.make(theta10000Samples09Discount14DiscreteActions).zDotProduct(h.make(((State)sq).getArray())));*/
        Util.initEnvironment();

        Trainer trainer = new Trainer("Trainer");
        trainer.connect();

        PerPlayModePolicy perPlayModePolicy = new PerPlayModePolicy(valueiterationPolicy);
        perPlayModePolicy.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        Team team1 = new Team(PitchSide.WEST, 2, new ChaseAndKickPolicy());
        team1.connectAll();

        Team team2 = new Team(PitchSide.EAST, 2, perPlayModePolicy);
        team2.connectAll();

        trainer.movePlayer(new PlayerState(PitchSide.WEST, 1, -50, 50));
        trainer.movePlayer(new PlayerState(PitchSide.WEST, 2, -50, -50));

        trainer.movePlayer(new PlayerState(PitchSide.EAST, 1, 5, -5));
        trainer.movePlayer(new PlayerState(PitchSide.EAST, 2, 5, 5));

        Thread.sleep(100);
        trainer.moveBall(new Ball(5, -5));
        trainer.changePlayMode(com.github.robocup_atan.atan.model.enums.PlayMode.PLAY_ON);
    }
}

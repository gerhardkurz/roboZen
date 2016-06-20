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

        ITransition t = new TransitionDet(games.get(0).getNumberPlayers(), games.get(0).getNumberofAllPlayers(), games.get(0).getStates().get(0).getDimension());
        DoubleFactory1D h = DoubleFactory1D.dense;
        //IPolicy valueiterationPolicy = new ValueIterationPolicy(h.make(theta10000Samples09Discount14discreteActionsConvergedSimpleRewardAgainImprovedTransitions), new Reward(2000,-2000,50, -50, 70, 170, -170, PitchSide.EAST), t);//new SimpleReward(2000, PitchSide.EAST), t);//
        Util.initEnvironment();

        Trainer trainer = new Trainer("Trainer");
        trainer.connect();

        PerPlayModePolicy perPlayModePolicy = new PerPlayModePolicy(new ChaseAndKickPolicy());//valueiterationPolicy);
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

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
import edu.kit.robocup.util.TransitionTestPolicy;
import edu.kit.robocup.util.Util;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static edu.kit.robocup.game.PlayMode.KICK_OFF_EAST;

/**
 * Created by dani on 13.06.2016.
 */
public class MainTestTransitions {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        //List<Game> games = GameReader.getGamesFromFiles("recordings/chaseAndKick", "recordings/chaseAndKick1", "recordings/chaseAndKick2", "recordings/random");

        //ITransition t = new Transition(games);
        //((Transition)t).setLearning("Transitions/save.txt");
        ITransition t1 = new TransitionDet();

        TransitionTestPolicy test = new TransitionTestPolicy(new ChaseAndKickPolicy(), t1);

        Util.initEnvironment();

        Trainer trainer = new Trainer("Trainer");
        trainer.connect();

        PerPlayModePolicy perPlayModePolicy = new PerPlayModePolicy(test);
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

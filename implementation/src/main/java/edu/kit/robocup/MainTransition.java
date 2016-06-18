package edu.kit.robocup;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.mdp.Reward;
import edu.kit.robocup.mdp.SimpleReward;
import edu.kit.robocup.mdp.ValueIteration;
import edu.kit.robocup.mdp.transition.Game;
import edu.kit.robocup.mdp.transition.Transition;
import edu.kit.robocup.mdp.transition.TransitionDet;
import edu.kit.robocup.recorder.GameReader;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainTransition {

    static Logger logger = Logger.getLogger(MainTransition.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        List<Game> games = GameReader.getGamesFromFiles("recordings/chaseAndKick", "recordings/chaseAndKick1", "recordings/chaseAndKick1", "recordings/random");
        ValueIteration v = new ValueIteration(new TransitionDet(2,4, games.get(0).getStates().get(0).getDimension()), new SimpleReward(2000, PitchSide.EAST));
        //ValueIteration v = new ValueIteration(new TransitionDet(2,4, games.get(0).getStates().get(0).getDimension()), new Reward(2000,-2000,50, -50, 70, 170, -170, PitchSide.EAST));
        //Transition t = new Transition(games);
        //t.setLearning("Transitions/save.txt");
        //ValueIteration v = new ValueIteration(t, new Reward(2000,-2000,50, -50, 70, 170, -170, PitchSide.EAST));
        IPolicy valueiterationPolicy = v.solve();
    }
}

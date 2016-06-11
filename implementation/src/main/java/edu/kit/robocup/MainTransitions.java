package edu.kit.robocup;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.mdp.Reward;
import edu.kit.robocup.mdp.ValueIteration;
import edu.kit.robocup.mdp.transition.Game;
import edu.kit.robocup.recorder.GameReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainTransitions {
    public static void main(String[] args) throws IOException, InterruptedException {
        List<Game> games = new ArrayList<Game>();
        GameReader r = new GameReader("recordings/chaseAndKick300");
        games.add(r.getGameFromFile());
        r = new GameReader("recordings/chaseAndKick3001");
        games.add(r.getGameFromFile());
        r = new GameReader("recordings/random300");
        games.add(r.getGameFromFile());
        ValueIteration v = new ValueIteration(games, new Reward(2000,-2000,50, -50, 70, 170, -170, PitchSide.EAST));
        IPolicy valueiterationPolicy = v.solve();
    }
}

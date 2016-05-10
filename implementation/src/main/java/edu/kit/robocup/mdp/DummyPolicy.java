package edu.kit.robocup.mdp;

import edu.kit.robocup.game.IPlayer;
import edu.kit.robocup.game.PlayerState;
import edu.kit.robocup.game.action.Action;
import edu.kit.robocup.game.action.Dash;
import edu.kit.robocup.game.action.Turn;

import java.util.*;


public class DummyPolicy implements IPolicy {
    private final String teamName;

    public DummyPolicy(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public Map<IPlayer, Action> getAction(IState state) {
        Map<IPlayer, Action> result = new TreeMap<>(new PlayerComparator());
        List<PlayerState> players = state.getPlayers(teamName);
        for (PlayerState player: players) {
            if (yoloGen(0.25f)) {
                result.put(player, new Turn(20));
            } else {
                result.put(player, new Dash(30));;
            }
        }
        return result;
    }

    private boolean yoloGen(float probability) {
        Random rnd = new Random();
        return rnd.nextFloat() <= probability;
    }
}





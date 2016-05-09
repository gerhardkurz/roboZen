package edu.kit.robocup.mdp;

import edu.kit.robocup.game.IPlayer;
import edu.kit.robocup.game.PlayerState;
import edu.kit.robocup.game.action.IAction;
import edu.kit.robocup.game.action.Yolo;
import edu.kit.robocup.mdp.IPolicy;
import edu.kit.robocup.mdp.IState;

import java.util.*;


public class DummyPolicy implements IPolicy {
    private final String teamName;

    public DummyPolicy(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public Map<IPlayer, IAction> getAction(IState state) {
        Map<IPlayer, IAction> result = new TreeMap<>(new PlayerComparator());
        List<PlayerState> players = state.getPlayers(teamName);
        for (PlayerState player: players) {
            result.put(player, new Yolo());
        }
        return result;
    }
}

package edu.kit.robocup.mdp;

import edu.kit.robocup.game.IPlayer;
import edu.kit.robocup.game.PlayerState;
import edu.kit.robocup.game.action.IAction;
import edu.kit.robocup.game.action.Yolo;
import edu.kit.robocup.mdp.IPolicy;
import edu.kit.robocup.mdp.IState;

import java.util.*;


public class DummyPolicy implements IPolicy {
    @Override
    public Map<IPlayer, IAction> getAction(IState state) {
        Map<IPlayer, IAction> result = new TreeMap<>(new Comparator<IPlayer>(){
            @Override
            public int compare(IPlayer o1, IPlayer o2) {
                int team = o1.getTeamName().compareTo(o2.getTeamName());
                if (team != 0) {
                    return team;
                } else {
                    return o1.getNumber() - o2.getNumber();
                }
            }
        });
        List<PlayerState> players = state.getPlayers("t1");
        for (PlayerState player: players) {
            result.put(player, new Yolo());
        }
        return result;
    }
}

package edu.kit.robocup.mdp;

import edu.kit.robocup.game.IPlayer;

import java.util.Comparator;


public class PlayerComparator implements Comparator<IPlayer> {
    @Override
    public int compare(IPlayer o1, IPlayer o2) {
        int team = o1.getTeamName().compareTo(o2.getTeamName());
        if (team != 0) {
            return team;
        } else {
            return o1.getNumber() - o2.getNumber();
        }
    }
}

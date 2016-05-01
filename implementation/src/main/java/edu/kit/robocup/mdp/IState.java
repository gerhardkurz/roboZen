package edu.kit.robocup.mdp;

import edu.kit.robocup.game.Ball;
import edu.kit.robocup.game.Player;
import edu.kit.robocup.game.Team;

import java.util.List;

public interface IState {
    Ball getBall();
    List<Player> getPlayers(final Team team);
}

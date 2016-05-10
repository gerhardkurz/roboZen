package edu.kit.robocup.mdp;

import edu.kit.robocup.game.Ball;
import edu.kit.robocup.game.PlayerState;

import java.util.List;

public interface IState {
    Ball getBall();
    List<PlayerState> getPlayers(final String teamName);
}

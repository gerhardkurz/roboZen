package edu.kit.robocup.mdp;

import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.IPlayerState;

import java.util.List;

public interface IState {
    Ball getBall();
    List<IPlayerState> getPlayers(final String teamName);
}

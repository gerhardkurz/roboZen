package edu.kit.robocup.mdp;

import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.IPlayerState;

import java.io.Serializable;
import java.util.List;

public interface IState extends Serializable {
    Ball getBall();
    List<IPlayerState> getPlayers(final String teamName);
}

package edu.kit.robocup.mdp;

import edu.kit.robocup.game.BallState;
import edu.kit.robocup.game.PlayerState;

import java.util.List;

public interface IState {
    BallState getBall();
    List<PlayerState> getPlayers(final String teamName);
}

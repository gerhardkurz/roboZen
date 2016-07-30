package edu.kit.robozen.interf.mdp;

import edu.kit.robozen.constant.PitchSide;
import edu.kit.robozen.game.PlayMode;
import edu.kit.robozen.interf.game.IPlayer;
import edu.kit.robozen.game.state.Ball;
import edu.kit.robozen.interf.game.IPlayerState;

import java.io.Serializable;
import java.util.List;

public interface IState extends Serializable {
    Ball getBall();
    List<IPlayerState> getPlayers();
    List<IPlayerState> getPlayers(final PitchSide pitchSide);
    IPlayerState getPlayerState(IPlayer player);
    PlayMode getPlayMode();
    IState flipPitchSide();
    IState normalizeStateToEast(PitchSide pitchSide);
}

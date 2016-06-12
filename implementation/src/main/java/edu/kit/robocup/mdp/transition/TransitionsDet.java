package edu.kit.robocup.mdp.transition;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.Dash;
import edu.kit.robocup.game.Kick;
import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.game.Turn;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.mdp.PlayerActionSet;

import java.util.List;

/**
 * Created by dani on 12.06.2016.
 */
public class TransitionsDet implements ITransitions {
    @Override
    public State getNewStateSample(State s, PlayerActionSet a, PitchSide pitchSide) {
        Ball b = s.getBall();
        List<IPlayerState> p = s.getPlayers(pitchSide);
        for (IPlayerState player : p) {
            PlayerAction actual = null;
            for (PlayerAction act : a.getActions()) {
                if (act.getPlayerNumber() == player.getNumber()) {
                    actual = act;
                }
            }
            switch (actual.getActionType()) {
                case KICK: {
                    break;
                }
                case DASH: {
                    break;
                }
                case TURN: {
                    break;
                }
                case MOVE: {
                    break;
                }
            }
        }
        return null;
    }
}

package edu.kit.robocup.tree;

import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.Dash;
import edu.kit.robocup.game.Kick;
import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.interf.game.IPlayer;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.interf.mdp.IState;
import edu.kit.robocup.mdp.PlayerActionSet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class ComplexKick implements IComplexAction {
    @Override
    public boolean isPossible(IState state, PitchSide pitchSide) {
        return state.getPlayers(pitchSide).stream()
                .filter(p -> p.canKickBall(state.getBall()))
                .findAny()
                .isPresent();
    }

    @Override
    public List<PlayerActionSet> getPlayerActionSets(IState state, PitchSide pitchSide) {
        state = state.normalizeStateToEast(pitchSide);
        List<PlayerAction> playerActions = new ArrayList<>(state.getPlayers(pitchSide).size());
        List<PlayerActionSet> result = new LinkedList<>();
        result.add(new PlayerActionSet(playerActions));
        for (IPlayerState player: state.getPlayers(pitchSide)) {
            if (player.canKickBall(state.getBall())) {
                double angle = player.getAngleTo(Constants.GOAL_WEST);
                playerActions.add(new PlayerAction(player.getNumber(), new Kick(100, (int) angle)));
            } else {
                //TODO run parallel
                playerActions.add(new PlayerAction(player.getNumber(), new Dash(100)));
            }
        }
        return result;
    }
}

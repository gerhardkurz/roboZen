package edu.kit.robocup.tree;

import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.Dash;
import edu.kit.robocup.game.Kick;
import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.game.Turn;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.interf.mdp.IState;
import edu.kit.robocup.mdp.PlayerActionSet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class ComplexRun implements IComplexAction {
    @Override
    public boolean isPossible(IState state, PitchSide pitchSide) {
        return true;
    }

    @Override
    public List<PlayerActionSet> getPlayerActionSets(IState state, PitchSide pitchSide) {
//        state = state.normalizeStateToEast(pitchSide);
        List<PlayerActionSet> result = new LinkedList<>();

        IPlayerState closestPlayer =  getClosestPlayer(state, pitchSide);
        double angle = closestPlayer.getAngleTo(state.getBall());
        if (Math.abs(angle) >= 5) {
            List<PlayerAction> firstPlayerActions = new ArrayList<>(state.getPlayers(pitchSide).size());
            firstPlayerActions.add(new PlayerAction(closestPlayer.getNumber(), new Turn((int)angle)));
            result.add(new PlayerActionSet(firstPlayerActions));
        }
        List<PlayerAction> playerActions = new ArrayList<>(state.getPlayers(pitchSide).size());
        playerActions.add(new PlayerAction(closestPlayer.getNumber(), new Dash(100)));
        result.add(new PlayerActionSet(playerActions));

        for (PlayerActionSet playerActionSet: result) {
            for (IPlayerState player: state.getPlayers(pitchSide)) {
                if (player != closestPlayer) {
                    playerActionSet.getActions().add(new PlayerAction(player.getNumber(), new Dash(100)));
                }
            }
        }
        return result;
    }

    private IPlayerState getClosestPlayer(IState state, PitchSide pitchSide) {
        IPlayerState closestPlayer = null;
        double maxDistance = Integer.MAX_VALUE;
        for (IPlayerState player: state.getPlayers(pitchSide)) {
            double distance = player.getDistance(state.getBall());
            if (distance < maxDistance) {
                closestPlayer = player;
                maxDistance = distance;
            }
        }
        return closestPlayer;
    }
}

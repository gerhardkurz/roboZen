package edu.kit.robozen.example.policy.heurisic;


import edu.kit.robozen.constant.Constants;
import edu.kit.robozen.constant.PitchSide;
import edu.kit.robozen.game.*;
import edu.kit.robozen.game.controller.IPlayerController;
import edu.kit.robozen.interf.game.IAction;
import edu.kit.robozen.interf.game.IPlayerState;
import edu.kit.robozen.interf.mdp.IPolicy;
import edu.kit.robozen.interf.mdp.IState;

import java.util.*;
import java.util.stream.Collectors;

public class KickOffPolicy implements IPolicy {
    private final List<SimpleGameObject> positionsAttack = new ArrayList<>();
    private final List<SimpleGameObject> positionsDefend = new ArrayList<>();

    {
        positionsAttack.add(new SimpleGameObject(0, 0));
        positionsAttack.add(new SimpleGameObject(1, 10));
        positionsAttack.add(new SimpleGameObject(10, 0));
        positionsAttack.add(new SimpleGameObject(20, 20));
        positionsAttack.add(new SimpleGameObject(20, -20));

        positionsDefend.add(new SimpleGameObject(10, 10));
        positionsDefend.add(new SimpleGameObject(10, -10));
        positionsDefend.add(new SimpleGameObject(20, 0));
        positionsDefend.add(new SimpleGameObject(20, 20));
        positionsDefend.add(new SimpleGameObject(20, -20));
    }


    @Override
    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> playerControllers, PitchSide pitchSide) {
        List<IPlayerState> playerStates = state.getPlayers(pitchSide);

        if (state.getPlayMode() == PlayMode.KICK_OFF_EAST && pitchSide == PitchSide.EAST || state.getPlayMode() == PlayMode.KICK_OFF_WEST && pitchSide == PitchSide.WEST) {
            return attack(playerStates, playerControllers, pitchSide);
        } else {
            return defend(playerControllers, playerStates, pitchSide);
        }
    }

    private Map<IPlayerController, IAction> attack(List<IPlayerState> playerStates, List<? extends IPlayerController> playerControllers, PitchSide pitchSide) {
        List<SimpleGameObject> positions = getPositions(positionsAttack, pitchSide);

        boolean inPositionForKickOff = true;
        for (int i = 0; i < playerStates.size(); i++) {
            inPositionForKickOff &= playerStates.get(i).getDistance(positions.get(i)) < Constants.KICKABLE_MARGIN;
        }
        if (inPositionForKickOff) {
            return kickOff(positions, playerControllers, playerStates);
        } else {
            Map<IPlayerController, IAction> action = new HashMap<>();
            for (int i = 0; i < positions.size() && i < playerControllers.size(); i++) {
                Optional<IAction> optionalAction = moveInPositionAndLookAt(positions.get(i), playerControllers.get(i), playerStates.get(i), new SimpleGameObject(0, 0));
                if (optionalAction.isPresent()) {
                    action.put(playerControllers.get(i), optionalAction.get());
                }
            }
            return action;
        }
    }

    private Map<IPlayerController, IAction> defend(List<? extends IPlayerController> playerControllers, List<IPlayerState> playerStates, PitchSide pitchSide) {
        List<SimpleGameObject> positions = getPositions(positionsDefend, pitchSide);

        Map<IPlayerController, IAction> action = new HashMap<>();
        for (int i = 0; i < playerStates.size(); i++) {
            IPlayerState playerState = playerStates.get(i);
            SimpleGameObject position = positions.get(i);
            Optional<IAction> optionalAction = moveInPositionAndLookAt(position, playerControllers.get(i), playerState, new SimpleGameObject(0, 0));
            if (optionalAction.isPresent()) {
                action.put(playerControllers.get(i), optionalAction.get());
            }
        }
        return action;
    }

    private List<SimpleGameObject> getPositions(List<SimpleGameObject> positions, PitchSide pitchSide) {
        if (pitchSide == PitchSide.EAST) {
            return positions;
        } else {
            return positions.stream().map(position -> new SimpleGameObject(-position.getPositionX(), -position.getPositionY())).collect(Collectors.toCollection(LinkedList::new));
        }
    }

    private Map<IPlayerController, IAction> kickOff(List<SimpleGameObject> positions, List<? extends IPlayerController> playerControllers, List<IPlayerState> playerStates) {
        Map<IPlayerController, IAction> action = new HashMap<>();
        double angleToPlayer2 = playerStates.get(0).getAngleTo(positions.get(1)) - 5;
        action.put(playerControllers.get(0), new Kick(30, (int) angleToPlayer2));
        return action;
    }


    private Optional<IAction> moveInPositionAndLookAt(SimpleGameObject position, IPlayerController playerController, IPlayerState playerState, SimpleGameObject lookAtTarget) {
        double angleToPosition = playerState.getAngleTo(position);
        if (playerState.getDistance(position) >= Constants.KICKABLE_MARGIN) {
            if (Math.abs(angleToPosition) >= 1) {
                return Optional.of(new Turn((int) angleToPosition));
            } else {
                return Optional.of(new Dash(40));
            }
        } else {
            double angleToLookAt = playerState.getAngleTo(lookAtTarget);
            if (Math.abs(angleToLookAt) > 1) {
                return Optional.of(new Turn((int) angleToLookAt));
            }
        }
        return Optional.empty();
    }
}
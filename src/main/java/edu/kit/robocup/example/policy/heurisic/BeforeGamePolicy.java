package edu.kit.robocup.example.policy.heurisic;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.*;
import edu.kit.robocup.game.controller.IPlayerController;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.interf.mdp.IState;

import java.util.*;


public class BeforeGamePolicy implements IPolicy {
    private final List<SimpleGameObject> positions = new ArrayList<>();
    private Set<IPlayerController> moved = new HashSet<>();

    {
        positions.add(new SimpleGameObject(-10, -10));
        positions.add(new SimpleGameObject(-10, 10));
        positions.add(new SimpleGameObject(-20, 0));
        positions.add(new SimpleGameObject(-20, -20));
        positions.add(new SimpleGameObject(-20, 20));
    }


    @Override
    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> playerControllers, PitchSide pitchSide) {
        Map<IPlayerController, IAction> result = new HashMap<>();
        List<IPlayerState> playerStates = state.getPlayers(pitchSide);
        for (int i = 0; i < playerControllers.size(); i++) {
            Optional<IAction> optionalAction = moveInPositionAndLookAt(positions.get(i), playerControllers.get(i), playerStates.get(i), new SimpleGameObject(0, 0));
            if (optionalAction.isPresent()) {
                result.put(playerControllers.get(i), optionalAction.get());
                moved.add(playerControllers.get(i));
            }
        }
        return result;
    }


    private Optional<IAction> moveInPositionAndLookAt(SimpleGameObject position, IPlayerController playerController, IPlayerState playerState, SimpleGameObject lookAtTarget) {
        if (!moved.contains(playerController)) {
            return Optional.of(new Move((int) position.getPositionX(), (int) position.getPositionY()));
        } else {
            double angleToLookAt = playerState.getAngleTo(lookAtTarget);
            if (Math.abs(angleToLookAt) > 1) {
                return Optional.of(new Turn((int) angleToLookAt));
            }
        }
        return Optional.empty();
    }
}
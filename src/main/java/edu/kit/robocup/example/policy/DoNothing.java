package edu.kit.robocup.example.policy;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.SimpleGameObject;
import edu.kit.robocup.game.Turn;
import edu.kit.robocup.game.controller.IPlayerController;
import edu.kit.robocup.game.controller.PlayerController;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.interf.mdp.IState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by dani on 03.06.2016.
 */
public class DoNothing implements IPolicy {
    @Override
    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> playerControllers, PitchSide pitchSide) {

        Map<IPlayerController, IAction> action = new HashMap<>();
        for (int i = 0; i < playerControllers.size(); i++) {
            action.put(playerControllers.get(i), new Turn(0));
        }
        return action;
    }
}

package edu.kit.robozen.example.policy;

import edu.kit.robozen.constant.PitchSide;
import edu.kit.robozen.game.Turn;
import edu.kit.robozen.game.controller.IPlayerController;
import edu.kit.robozen.interf.game.IAction;
import edu.kit.robozen.interf.mdp.IPolicy;
import edu.kit.robozen.interf.mdp.IState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

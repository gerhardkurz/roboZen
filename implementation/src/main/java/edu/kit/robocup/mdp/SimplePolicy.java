package edu.kit.robocup.mdp;

import edu.kit.robocup.game.Dash;
import edu.kit.robocup.game.IAction;
import edu.kit.robocup.game.IPlayer;
import edu.kit.robocup.game.Turn;
import edu.kit.robocup.game.controller.IPlayerController;

import java.util.*;


public class SimplePolicy implements IPolicy {

    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> players) {
        Map<IPlayerController, IAction> action = new HashMap<>();
        for (IPlayerController playerController : players) {
            if (yoloGen(0.25f)) {
                action.put(playerController, new Turn(20));
            } else {
                action.put(playerController, new Dash(30));
            }
        }
        return action;
    }


    private boolean yoloGen(float probability) {
        Random rnd = new Random();
        return rnd.nextFloat() <= probability;
    }
}





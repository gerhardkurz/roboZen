package edu.kit.robozen.example.policy;

import edu.kit.robozen.constant.PitchSide;
import edu.kit.robozen.game.Dash;
import edu.kit.robozen.game.Kick;
import edu.kit.robozen.game.Turn;
import edu.kit.robozen.game.controller.IPlayerController;
import edu.kit.robozen.interf.game.IAction;
import edu.kit.robozen.interf.mdp.IPolicy;
import edu.kit.robozen.interf.mdp.IState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Policy with reduced Actions, only allowed: Turn 1, Turn 10, Turn 50, Turn -1, Turn -10, Turn -50, Dash 40, Kick (30,0), Kick (30,25), Kick(30, -25)
 */
public class AllActionCombinationsPolicyReduced implements IPolicy {
    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> players, PitchSide pitchSide) {
        Map<IPlayerController, IAction> action = new HashMap<>();
        for (IPlayerController playerController : players) {
            if (yoloGen(0.33f)) {
                if (yoloGen(0.33f)) {
                    action.put(playerController, new Kick(30, 0));
                } else {
                    if (yoloGen(0.5f)) {
                        action.put(playerController, new Kick(30, -25));
                    } else {
                        action.put(playerController, new Kick(30, 25));
                    }
                }
            } else if (yoloGen(0.5f)){
                if (yoloGen(0.5f)) {
                    if (yoloGen(0.33f)) {
                        action.put(playerController, new Turn(1));
                    } else if (yoloGen(0.5f)) {
                        action.put(playerController, new Turn(10));
                    } else {
                        action.put(playerController, new Turn(50));
                    }
                } else if (yoloGen(0.33f)) {
                    action.put(playerController, new Turn(-1));
                } else if (yoloGen(0.5f)) {
                    action.put(playerController, new Turn(-10));
                } else {
                    action.put(playerController, new Turn(-50));
                }
            } else {
                action.put(playerController, new Dash(40));
            }
        }
        return action;
    }


    private boolean yoloGen(float probability) {
        Random rnd = new Random();
        return rnd.nextFloat() <= probability;
    }
}

package edu.kit.robocup.mdp;

import edu.kit.robocup.game.controller.IPlayerController;

import java.util.*;


public class SimplePolicy implements IPolicy {

    public void apply(IState state, List<? extends IPlayerController> playerControllers) {
        for (IPlayerController playerController : playerControllers) {
            if (yoloGen(0.25f)) {
                playerController.turn(20);
            } else {
                playerController.dash(30);
            }
        }
    }


    private boolean yoloGen(float probability) {
        Random rnd = new Random();
        return rnd.nextFloat() <= probability;
    }
}





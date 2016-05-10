package edu.kit.robocup.mdp;

import edu.kit.robocup.game.IPlayerController;

import java.util.*;


public class DummyPolicy implements IPolicy {
    private final String teamName;

    public DummyPolicy(String teamName) {
        this.teamName = teamName;
    }

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





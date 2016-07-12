package edu.kit.robocup.game.controller;


import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.mdp.PlayerActionSet;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Team {
    private static final Logger logger = Logger.getLogger(Team.class);
    private PitchSide pitchSide;
    private final IPolicy policy;
    private List<PlayerController> playerControllers = new ArrayList<>();
    private Coach coach;


    public Team(PitchSide pitchSide, int numberPlayers, IPolicy policy) {
        this.pitchSide = pitchSide;
        this.policy = policy;
        for (int i = 0; i < numberPlayers; i++) {
            playerControllers.add(new PlayerController(this, i + 1));
        }
        coach = new Coach(this);
    }

    void handleState(State state) {
        Map<IPlayerController, IAction> actions = policy.apply(state, playerControllers, pitchSide);
        actions.entrySet().forEach(e -> e.getKey().execute(e.getValue()));
    }

    public PlayerActionSet simulateHandleState(State state) {
        Map<IPlayerController, IAction> actions = policy.apply(state, playerControllers, pitchSide);
        return new PlayerActionSet(actions.values());
    }

    public PlayerController getPlayer(int number) {
        return playerControllers.stream().filter(p -> p.getNumber() == number).findAny().get();
    }


    public Coach getCoach() {
        return coach;
    }



    public void connectAll() {
        doForEach(TeamAction.CONNECT);
        coach.connect();
    }

    public void reconnectAll() {
        doForEach(TeamAction.RECONNECT);
        coach.connect();
    }

    public void killAll() {
        doForEach(TeamAction.KILL);
        coach.bye();
    }

    private void doForEach(TeamAction action) {
        playerControllers.stream().forEach(p -> {
            try {
                doForPlayer(action, p);
            } catch (Exception ex) {
                p.handleError(ex.getMessage());
            }
            pause(500);
        });
    }

    private void doForPlayer(TeamAction action, PlayerController playerController) {
        switch (action) {
            case CONNECT:
                playerController.connect(false);
                break;
            case RECONNECT:
                playerController.reconnect();
                break;
            case KILL:
                playerController.bye();
                break;
        }
    }


    /**
     * Pause the thread.
     *
     * @param ms How long to pause the thread for (in ms).
     */
    private synchronized void pause(int ms) {
        try {
            this.wait(ms);
        } catch (InterruptedException ex) {
            logger.warn("Interrupted Exception ", ex);
        }
    }

    private enum TeamAction {
        CONNECT, RECONNECT, KILL;
    }

    public PitchSide getPitchSide() {
        return pitchSide;
    }

    public void setPitchSide(PitchSide pitchSide) {
        this.pitchSide = pitchSide;
    }
}

package edu.kit.robocup.game;


import edu.kit.robocup.mdp.IPolicy;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class Team {
    private static final Logger logger = Logger.getLogger(Team.class);
    private final IPolicy policy;
    private final String teamName;
    protected boolean isTeamEast;
    private List<PlayerController> playerControllers = new ArrayList<>();
    private Coach coach;


    public Team(String teamName, int numberPlayers, IPolicy policy) {
        this.teamName = teamName;
        this.policy = policy;
        for (int i = 0; i < numberPlayers; i++) {
            playerControllers.add(new PlayerController(this, i + 1));
        }
        coach = new Coach(this);
    }

    public void handleState(State state) {
        policy.apply(state, playerControllers);
    }


    public PlayerController getPlayer(int number) {
        return playerControllers.stream().filter(p -> p.getNumber() == number).findAny().get();
    }

    public String getTeamName() {
        return teamName;
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

    public boolean isTeamEast() {
        return isTeamEast;
    }

    public void setTeamEast(boolean teamEast) {
        isTeamEast = teamEast;
    }
}

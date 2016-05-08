package edu.kit.robocup.game.intf.client;


import com.github.robocup_atan.atan.model.ActionsCoach;
import com.github.robocup_atan.atan.model.ActionsPlayer;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


//~--- non-JDK imports --------------------------------------------------------


/**
 * An abstract class to use for teams.
 *
 * @author Atan
 */
public class Team {
    private static final Logger log = Logger.getLogger(Team.class);

    private List<Player> players = new ArrayList<>();
    private Coach coach;
    private String teamName;


    public Team(String teamName, int numberPlayers) {
        for (int i = 0; i < numberPlayers; i++) {
            players.add(new Player(teamName, i + 1));
        }
        coach = new Coach(teamName);
    }

    public ActionsPlayer getPlayerOutput(int index) {
        return players.get(index);
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
        players.stream().forEach(p -> {
            try {
                doForPlayer(action, p);
            } catch (Exception ex) {
                p.handleError(ex.getMessage());
            }
            pause(500);
        });
    }

    private void doForPlayer(TeamAction action, Player player) {
        switch (action) {
            case CONNECT:
                player.connect(false);
                break;
            case RECONNECT:
                player.reconnect();
                break;
            case KILL:
                player.bye();
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
            log.warn("Interrupted Exception ", ex);
        }
    }

    private enum TeamAction {
        CONNECT, RECONNECT, KILL;
    }
}

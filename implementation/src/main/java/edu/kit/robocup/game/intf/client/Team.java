package edu.kit.robocup.game.intf.client;


import com.github.robocup_atan.atan.model.ActionsCoach;
import com.github.robocup_atan.atan.model.ActionsPlayer;
import edu.kit.robocup.game.intf.output.PlayerOutput;
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
    private static final int COACH_PORT = 6002;
    private static final int TRAINER_PORT = 6001;

    private String hostname = "localhost";
    private int playerPort = 6000;

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
        return players.get(index).getOutput();
    }

    public ActionsCoach getCoachOutput() {
        return coach.getOutput();
    }



    public void connectAll() {
        doForEach(TeamAction.CONNECT);
        coach.getOutput().connect();
    }

    public void reconnectAll() {
        doForEach(TeamAction.RECONNECT);
        coach.getOutput().connect();
    }

    public void killAll() {
        doForEach(TeamAction.KILL);
        coach.getOutput().bye();
    }

    private void doForEach(TeamAction action) {
        players.stream().map(Player::getOutput).forEach(output -> {
            try {
                doForPlayer(action, output);
            } catch (Exception ex) {
                output.handleError(ex.getMessage());
            }
            pause(500);
        });
    }

    private void doForPlayer(TeamAction action, PlayerOutput player) {
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

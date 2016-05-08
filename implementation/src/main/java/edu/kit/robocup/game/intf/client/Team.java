package edu.kit.robocup.game.intf.client;


import com.github.robocup_atan.atan.model.ActionsPlayer;
import edu.kit.robocup.game.IPlayer;
import edu.kit.robocup.game.PlayerState;
import edu.kit.robocup.game.State;
import edu.kit.robocup.game.action.IAction;
import edu.kit.robocup.mdp.IPolicy;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Team {
    private static final Logger logger = Logger.getLogger(Team.class);
    private final IPolicy policy;
    private final String teamName;
    private List<Player> players = new ArrayList<>();
    private Coach coach;


    public Team(String teamName, int numberPlayers, IPolicy policy) {
        this.teamName = teamName;
        this.policy = policy;
        for (int i = 0; i < numberPlayers; i++) {
            players.add(new Player(this, i + 1));
        }
        coach = new Coach(this);
    }

    public void handleState(State state) {
        logger.info(state);
        Map<IPlayer, IAction> action = policy.getAction(state);
        executeAction(action);
    }

    private void executeAction(Map<IPlayer, IAction> action) {
        for(Player player: players) {
            try {
                player.send(action.get(player).getCommandString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTeamName() {
        return teamName;
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
            logger.warn("Interrupted Exception ", ex);
        }
    }

    private enum TeamAction {
        CONNECT, RECONNECT, KILL;
    }
}

package edu.kit.robocup.game.state;

import edu.kit.robocup.mdp.IState;

import java.util.List;
import java.util.stream.Collectors;

public class State implements IState {
    private final Ball ball;
    private final List<IPlayerState> players;

    public State(Ball ball, List<IPlayerState> players) {
        this.ball = ball;
        this.players = players;
    }

    public Ball getBall() {
        return ball;
    }

    public List<IPlayerState> getPlayers(final String teamName) {
        return players.stream().filter(p -> p.getTeamName().equals(teamName)).collect(Collectors.toList());
    }
    
    public int getDimension() {
    	return players.size() + 1;
    }

    @Override
    public String toString() {
        return "State{" + ball.toString() + " " + players.toString() + "}";
    }
}

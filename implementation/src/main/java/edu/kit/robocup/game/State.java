package edu.kit.robocup.game;

import edu.kit.robocup.mdp.IState;

import java.util.List;
import java.util.stream.Collectors;

public class State implements IState {
    private final BallState ball;
    private final List<PlayerState> players;
    int dim;

    public State(BallState ball, List<PlayerState> players) {
        this.ball = ball;
        this.players = players;
        dim = 1 + players.size();
    }

    public BallState getBall() {
        return ball;
    }

    public List<PlayerState> getPlayers(final String teamName) {
        return players.stream().filter(p -> p.getTeamName().equals(teamName)).collect(Collectors.toList());
    }
    
    public int getDimension() {
    	return this.dim;
    }

    @Override
    public String toString() {
        return "State{" + ball.toString() + " " + players.toString() + "}";
    }
}

package edu.kit.robocup.game;

import edu.kit.robocup.mdp.IState;

import java.util.List;
import java.util.stream.Collectors;

public class State implements IState {
    private final BallState ball;
    private final List<PlayerState> players;

    public State(BallState ball, List<PlayerState> players) {
        this.ball = ball;
        this.players = players;
    }

    public BallState getBall() {
        return ball;
    }

    public List<PlayerState> getPlayers(final String teamName) {
        return players.stream().filter(p -> p.getTeamName() == teamName).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "State{" + ball.toString() + " " + players.toString() + "}";
    }
}

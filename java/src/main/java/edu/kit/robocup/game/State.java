package edu.kit.robocup.game;

import edu.kit.robocup.mdp.IState;

import java.util.List;
import java.util.stream.Collectors;

public class State implements IState {
    private final Ball ball;
    private final List<Player> players;

    public State(Ball ball, List<Player> players) {
        this.ball = ball;
        this.players = players;
    }

    public Ball getBall() {
        return ball;
    }

    public List<Player> getPlayers(final Team team) {
        return players.stream().filter(p -> p.getTeam() == team).collect(Collectors.toList());
    }
}

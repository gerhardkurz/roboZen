package edu.kit.robocup.game;

import edu.kit.robocup.mdp.IState;

import java.util.List;
import java.util.stream.Collectors;

public class State implements IState {
    private final Ball ball;
    private final List<PlayerState> players;

    public State(Ball ball, List<PlayerState> players) {
        this.ball = ball;
        this.players = players;
    }

    public Ball getBall() {
        return ball;
    }

    public List<PlayerState> getPlayers(final String teamName) {
        return players.stream().filter(p -> p.isTeam(teamName)).collect(Collectors.toList());
    }
    
    public int getDimension() {
    	return players.size() + 1;
    }

    @Override
    public String toString() {
        return "State{" + ball.toString() + " " + players.toString() + "}";
    }
    
    public double[] getArray() {
		double[] pos = new double[2*(players.size() + 1)];
		pos[0] = ball.getPositionX();
		pos[1] = ball.getPositionY();
		for (int i = 0; i < players.size(); i++) {
			pos[2*(i+1)] = players.get(i).getPositionX();
			pos[2*(i+1)+1] = players.get(i).getPositionY();
		}
		return pos;
	}
}

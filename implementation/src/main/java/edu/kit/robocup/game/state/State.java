package edu.kit.robocup.game.state;

import edu.kit.robocup.mdp.IState;

import java.io.Serializable;
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

    public int getPlayerCount() {
        return players.size();
    }

    /**
     * @returns Dimension of state. Players get x and y coordinates, ball gets x, y coordinate and x, y velocity
     */
    public int getDimension() {
    	return 2*(players.size() + 2);
    }

    @Override
    public String toString() {
        return "State{" + ball.toString() + " " + players.toString() + "}";
    }

    /**
     * @return Array containing all x-/y-coordinates of players and the last
     * entries are ballposition and ballvelocity
     */
    public double[] getArray() {
		double[] pos = new double[2*(players.size() + 2)];
		for (int i = 0; i < players.size(); i++) {
			pos[2*i] = players.get(i).getPositionX();
			pos[2*i+1] = players.get(i).getPositionY();
		}
		pos[2*(players.size())] = ball.getPositionX();
		pos[2*(players.size())+1] = ball.getPositionY();
		pos[2*(players.size()+1)] = ball.getVelocityX();
		pos[2*(players.size()+1)+1] = ball.getVelocityY();
		return pos;
	}
}

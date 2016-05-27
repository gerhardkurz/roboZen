package edu.kit.robocup.game.state;

import edu.kit.robocup.interf.game.IPlayer;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.interf.mdp.IState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class State implements IState {
    private final Ball ball;
    private final List<IPlayerState> players;

    public State(Ball ball, List<IPlayerState> players) {
        this.ball = ball;
        this.players = players;
    }

    public State(double[] state, String teamname) {
        players = new ArrayList<>();
        for (int i = 0; i < state.length/5; i++) {
            players.add(new PlayerState(teamname, i, state[i], state[i+1], state[i+2], state[i+3], state[i+4], 0));
        }
        int cut = (state.length/5)*5;
        ball = new Ball(state[cut], state[cut+1], state[cut+2], state[cut+3]);
    }

    public Ball getBall() {
        return ball;
    }

    public List<IPlayerState> getPlayers(final String teamName) {
        return players.stream().filter(p -> p.getTeamName().equals(teamName)).collect(Collectors.toList());
    }

    @Override
    public IPlayerState getPlayerState(IPlayer player) {
        return players.stream().filter(p -> p.equals(player)).findAny().get();
    }

    public int getPlayerCount() {
        return players.size();
    }

    /**
     * @returns Dimension of state. Players get x and y coordinates, x, y velocity and body angle, ball gets x, y coordinate and x, y velocity
     */
    public int getDimension() {
    	return 5*(players.size()) + 4;
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
		double[] pos = new double[5*(players.size()) + 4];
		for (int i = 0; i < players.size(); i++) {
			pos[5*i] = players.get(i).getPositionX();
			pos[5*i+1] = players.get(i).getPositionY();
            pos[5*i+2] = players.get(i).getVelocityX();
            pos[5*i+3] = players.get(i).getVelocityY();
            pos[5*i+4] = players.get(i).getBodyAngle();
        }
		pos[5*(players.size())] = ball.getPositionX();
		pos[5*(players.size())+1] = ball.getPositionY();
		pos[5*(players.size()) +2] = ball.getVelocityX();
		pos[5*(players.size())+3] = ball.getVelocityY();
		return pos;
    }
}

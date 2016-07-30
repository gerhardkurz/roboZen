package edu.kit.robozen.game.state;

import edu.kit.robozen.constant.PitchSide;
import edu.kit.robozen.game.PlayMode;
import edu.kit.robozen.interf.game.IPlayer;
import edu.kit.robozen.interf.game.IPlayerState;
import edu.kit.robozen.interf.mdp.IState;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


public class State implements IState, Serializable {
    private PlayMode playMode;
    private final Ball ball;
    private final Set<IPlayerState> players = new TreeSet<>((Comparator<? super IPlayerState> & Serializable) (p1, p2) -> {
        if (p1.getPitchSide() != p2.getPitchSide()) {
            return p1.getPitchSide() == PitchSide.EAST ? -1 : 1;
        } else {
            return p1.getNumber() - p2.getNumber();
        }
    });
    private final double[] pos;

    public State(Ball ball, List<IPlayerState> players) {
        this.playMode = PlayMode.UNKNOWN;
        this.ball = ball;
        this.players.addAll(players);
        pos = new double[4*(players.size()+1)];
        initArray();
    }

    public List<IPlayerState> getPlayers() {
        return (players.stream().collect(Collectors.toList()));
    }

    public State(double[] state, PitchSide pitchSide, int numberOfPlayersOfPitchside) {
        this.playMode = PlayMode.UNKNOWN;
        int counter = 0;
        for (int i = 0; i < state.length/4 - 1; i++) {
            counter++;
            if (counter <= numberOfPlayersOfPitchside) {
                players.add(new PlayerState(pitchSide, i+1, state[i], state[i + 1], state[i + 2], state[i + 3], 0));
            } else {
                if (pitchSide == PitchSide.EAST) {
                    players.add(new PlayerState(PitchSide.WEST, i+1-numberOfPlayersOfPitchside, state[i], state[i + 1], state[i + 2], state[i + 3], 0));
                } else {
                    players.add(new PlayerState(PitchSide.EAST, i+1-numberOfPlayersOfPitchside, state[i], state[i + 1], state[i + 2], state[i + 3], 0));
                }
            }
        }

        ball = new Ball(state[state.length-4], state[state.length-3], state[state.length-2], state[state.length-1]);
        pos = new double[4*(players.size()+1)];
        initArray();
    }

    public State(double[] currentDistance, int numberPlayersPitchside, int numberAllPlayers, PitchSide pitchSide) {
        ball = new Ball(currentDistance[0], currentDistance[1], currentDistance[2], currentDistance[3]);
        PitchSide currentPitchSide = pitchSide;

        for (int p = 0; p < numberAllPlayers; p++) {
            if (p == numberPlayersPitchside)
                currentPitchSide = flipPitchSide(currentPitchSide);
            int pOffset = 4 + (p * 5);
            players.add(new PlayerState(pitchSide, p+1, currentDistance[pOffset], currentDistance[pOffset + 1], currentDistance[pOffset + 2], currentDistance[pOffset + 3], currentDistance[pOffset + 4], 0));
        }
        pos = new double[4*(players.size()+1)];
        initArray();
    }

    private PitchSide flipPitchSide(PitchSide pitchSide) {
        if (pitchSide == PitchSide.EAST)
            return PitchSide.WEST;
        return PitchSide.EAST;
    }

    public Ball getBall() {
        return ball;
    }

    public List<IPlayerState> getPlayers(final PitchSide pitchSide) {
        return players.stream().filter(p -> p.getPitchSide() == pitchSide).collect(Collectors.toList());
    }

    @Override
    public IPlayerState getPlayerState(IPlayer player) {
        return players.stream().filter(p -> p.equals(player)).findAny().get();
    }

    public int getPlayerCount() {
        return players.size();
    }

    /**
     * @return Dimension of state. Players get x and y coordinates, x, y velocity and body angle, ball gets x, y coordinate and x, y velocity
     */
    public int getDimension() {
    	return 4*(players.size()+1);
    }

    @Override
    public String toString() {
        return "State{PlayMode: " + playMode + " " + ball.toString() + " " + players.toString() + "}";
    }

    @Override
    public PlayMode getPlayMode() {
        return playMode;
    }

    public void setPlayMode(PlayMode playMode) {
        if (playMode == null)
            this.playMode = PlayMode.UNKNOWN;
        else {
            this.playMode = playMode;
        }
    }

    /**
     * @return Array containing all x-/y-coordinates of players and the last
     * entries are ballposition and ballvelocity
     */
    public double[] getArray() {
        return pos;
    }

    private void initArray() {
        int i = 0;
		for (IPlayerState player: players) {
			pos[4 * i] = player.getPositionX();
			pos[4 * i + 1] = player.getPositionY();
            pos[4 * i + 2] = player.getVelocityLength();
            pos[4 * i + 3] = player.getBodyAngle();
            i++;
        }
		pos[4 * players.size()] = ball.getPositionX();
		pos[4 * players.size() + 1] = ball.getPositionY();
		pos[4 * players.size() + 2] = ball.getVelocityX();
        pos[4 * players.size() + 3] = ball.getVelocityY();
    }

    public IState normalizeStateToEast(PitchSide pitchSide) {
        return pitchSide == PitchSide.WEST ? flipPitchSide() : this;
    }


    public IState flipPitchSide() {
        Ball ball = new Ball(-this.ball.getPositionX(), -this.ball.getPositionY(), -this.ball.getVelocityX(), -this.ball.getVelocityY());
        ArrayList<IPlayerState> players = new ArrayList<>();
        players.addAll(this.players.stream()
                .map(IPlayerState::flipPitchSide)
                .collect(Collectors.toList()));
        return new State(ball, players);
    }
}

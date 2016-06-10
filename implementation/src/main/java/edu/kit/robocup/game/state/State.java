package edu.kit.robocup.game.state;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.PlayMode;
import edu.kit.robocup.interf.game.IPlayer;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.interf.mdp.IState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class State implements IState {
    private PlayMode playMode;
    private final Ball ball;
    private final List<IPlayerState> players;

    public State(Ball ball, List<IPlayerState> players) {
        this.playMode = PlayMode.UNKNOWN;
        this.ball = ball;
        // sort first east side, than west side, each side sorted by player number
        Collections.sort(players, new Comparator<IPlayerState>() {
            @Override
            public int compare(IPlayerState o1, IPlayerState o2) {
                if (o1.getPitchSide() == PitchSide.EAST) {
                    if (o2.getPitchSide() == PitchSide.EAST) {
                        if (o1.getNumber() > o2.getNumber()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                } else {
                    if (o2.getPitchSide() == PitchSide.EAST) {
                        return 1;
                    } else {
                        if (o1.getNumber() > o2.getNumber()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                }
            }
        });
        this.players = players;
    }

    public State(double[] state, PitchSide pitchSide, int numberOfPlayersOfPitchside) {
        this.playMode = PlayMode.UNKNOWN;
        players = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < state.length/5; i++) {
            counter++;
            if (counter <= numberOfPlayersOfPitchside) {
                players.add(new PlayerState(pitchSide, i, state[i], state[i + 1], state[i + 2], state[i + 3], state[i + 4], 0));
            } else {
                if (pitchSide == PitchSide.EAST) {
                    players.add(new PlayerState(PitchSide.WEST, i, state[i], state[i + 1], state[i + 2], state[i + 3], state[i + 4], 0));
                } else {
                    players.add(new PlayerState(PitchSide.EAST, i, state[i], state[i + 1], state[i + 2], state[i + 3], state[i + 4], 0));
                }
            }
        }
        Collections.sort(players, new Comparator<IPlayerState>() {
            @Override
            public int compare(IPlayerState o1, IPlayerState o2) {
                if (o1.getPitchSide() == PitchSide.EAST) {
                    if (o2.getPitchSide() == PitchSide.EAST) {
                        if (o1.getNumber() > o2.getNumber()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                } else {
                    if (o2.getPitchSide() == PitchSide.EAST) {
                        return 1;
                    } else {
                        if (o1.getNumber() > o2.getNumber()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                }
            }
        });
        int cut = (state.length/5)*5;
        ball = new Ball(state[cut], state[cut+1], state[cut+2], state[cut+3]);
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
    	return 5*(players.size()) + 4;
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

    public void clearPlayerStates() {
        players.clear();
    }
}

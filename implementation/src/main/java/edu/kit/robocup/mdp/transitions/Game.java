package edu.kit.robocup.mdp.transitions;

import java.util.List;

import edu.kit.robocup.game.state.State;
import edu.kit.robocup.mdp.IActions;

public class Game implements IGame {

	// states s_0 to s_T
	private List<State> states;
	
	// actions a_0 to a_(T-1), a_i is action between s_i and s_(i+1)
	private List<IActions> actions;
	
	// number of players of Team that is doing actions
	private int numberPlayers;
	
	// number of timesteps of a game, T
	private int gamelength;
	
	public Game(List<State> states, List<IActions> actions, int numberPlayers) {
		this.states = states;
		this.actions = actions;
		this.numberPlayers = numberPlayers;
	}
	
	public int getNumberPlayers() {
		return this.numberPlayers;
	}
	
	@Override
	public List<State> getStates() {
		return this.states;
	}

	@Override
	public List<IActions> getActions() {
		return this.actions;
	}

	public int getGamelength() {
		return this.gamelength;
	}

}

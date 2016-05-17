package edu.kit.robocup.mdp.transitions;

import java.util.List;

import edu.kit.robocup.game.state.State;
import edu.kit.robocup.mdp.IActionSet;

public class Game {

	// states s_0 to s_T
	private List<State> states;
	
	// actions a_0 to a_(T-1), a_i is action between s_i and s_(i+1)
	private List<IActionSet> actions;
	
	// number of players of Team that is doing actions
	private int numberPlayers;
	
	// number of timesteps of a game, T
	private int gamelength;
	
	public Game(List<State> states, List<IActionSet> actions) {
		this.states = states;
		this.actions = actions;
		this.numberPlayers = actions.get(0).getActions().size();
		this.gamelength = states.size();
	}
	
	public int getNumberPlayers() {
		return this.numberPlayers;
	}

	public List<State> getStates() {
		return this.states;
	}

	public List<IActionSet> getActions() {
		return this.actions;
	}

	public int getGamelength() {
		return this.gamelength;
	}

}

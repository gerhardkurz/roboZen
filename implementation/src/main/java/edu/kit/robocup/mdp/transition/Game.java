package edu.kit.robocup.mdp.transition;

import java.util.List;

import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.mdp.PlayerActionSet;

import static org.javacc.parser.LexGen.actions;

public class Game {

	// states s_0 to s_T
	private List<State> states;
	
	// actions a_0 to a_(T-1), a_i is action between s_i and s_(i+1)
	private List<PlayerActionSet> playerActionSets;
	
	// number of players of PitchSide that is doing actions
	private int numberPlayers;
	
	// number of timesteps of a game, T
	private int gamelength;
	
	public Game(List<State> states, List<PlayerActionSet> playerActionSets) {
		this.states = states;
		this.playerActionSets = playerActionSets;
		this.numberPlayers = playerActionSets.get(0).getActions().size();
		this.gamelength = states.size();
	}
	
	public int getNumberPlayers() {
		return this.numberPlayers;
	}

	public int getNumberofAllPlayers() {
		return states.get(0).getPlayerCount();
	}

	public List<State> getStates() {
		return this.states;
	}

	public List<PlayerActionSet> getActions() {
		return this.playerActionSets;
	}

	public int getGamelength() {
		return this.gamelength;
	}

}

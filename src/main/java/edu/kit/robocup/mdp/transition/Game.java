package edu.kit.robocup.mdp.transition;

import java.util.ArrayList;
import java.util.List;

import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.mdp.PlayerActionSet;

import static org.javacc.parser.LexGen.actions;

public class Game {

	// states s_0 to s_T
	private List<State> states = new ArrayList<>();
	
	// actions a_0 to a_(T-1), a_i is action between s_i and s_(i+1)
	private List<PlayerActionSet> playerActionSets = new ArrayList<>();
	
	// number of players of PitchSide that is doing actions
	private int numberPlayers;

	public Game(State state1, PlayerActionSet playerActionSet, State state2) {
		states.add(state1);
		states.add(state2);
		playerActionSets.add(playerActionSet);
		this.numberPlayers = playerActionSet.getActions().size();
	}

	public Game(List<State> states, List<PlayerActionSet> playerActionSets) {
		this.states = states;
		this.playerActionSets = playerActionSets;
		this.numberPlayers = playerActionSets.get(0).getActions().size();
	}
	
	public int getNumberPlayers() {
		return numberPlayers;
	}

	public int getNumberofAllPlayers() {
		return states.get(0).getPlayerCount();
	}

	public List<State> getStates() {
		return states;
	}

	public List<PlayerActionSet> getActions() {
		return playerActionSets;
	}

	public int getGamelength() {
		return states.size();
	}

}

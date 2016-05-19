package edu.kit.robocup.mdp.transition;

import edu.kit.robocup.game.IAction;
import edu.kit.robocup.game.controller.IPlayerController;
import edu.kit.robocup.game.state.State;

import java.util.List;
import java.util.Map;

public class GameMap {

	// states s_0 to s_T
	private List<State> states;

	// actions a_0 to a_(T-1), a_i is action between s_i and s_(i+1)
	private List<Map<IPlayerController, IAction>> actions;

	// number of players of Team that is doing actions
	private int numberPlayers;

	// number of timesteps of a game, T
	private int gamelength;

	private String team;

	public GameMap(List<State> states, List<Map<IPlayerController, IAction>> actions, String team) {
		this.states = states;
		this.actions = actions;
		this.team = team;
		this.numberPlayers = actions.get(0).size();
		this.gamelength = states.size();
	}
	
	public int getNumberPlayers() {
		return this.numberPlayers;
	}

	public String getTeam() {
		return this.team;
	}

	public List<State> getStates() {
		return this.states;
	}

	public List<Map<IPlayerController, IAction>> getActions() {
		return this.actions;
	}

	public int getGamelength() {
		return this.gamelength;
	}

}

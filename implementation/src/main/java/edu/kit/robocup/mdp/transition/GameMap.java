package edu.kit.robocup.mdp.transition;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.game.controller.IPlayerController;
import edu.kit.robocup.game.state.State;

import java.util.List;
import java.util.Map;

public class GameMap {

	// states s_0 to s_T
	private List<State> states;

	// actions a_0 to a_(T-1), a_i is action between s_i and s_(i+1)
	private List<Map<IPlayerController, IAction>> actions;

	// number of players of PitchSide that is doing actions
	private int numberPlayers;

	// number of timesteps of a game, T
	private int gamelength;

	private PitchSide pitchSide;

	public GameMap(List<State> states, List<Map<IPlayerController, IAction>> actions, PitchSide pitchSide) {
		this.states = states;
		this.actions = actions;
		this.pitchSide = pitchSide;
		this.numberPlayers = actions.get(0).size();
		this.gamelength = states.size();
	}
	
	public int getNumberPlayers() {
		return numberPlayers;
	}

	public PitchSide getPitchSide() {
		return pitchSide;
	}

	public List<State> getStates() {
		return states;
	}

	public List<Map<IPlayerController, IAction>> getActions() {
		return actions;
	}

	public int getGamelength() {
		return gamelength;
	}

}

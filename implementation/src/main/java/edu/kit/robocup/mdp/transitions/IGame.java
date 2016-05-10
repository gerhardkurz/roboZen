package edu.kit.robocup.mdp.transitions;

import java.util.List;

import edu.kit.robocup.game.state.State;
import edu.kit.robocup.mdp.IActions;

public interface IGame {
	// returns sequence of T states
	List<State> getStates();
	// returns sequence of T-1 actions
	List<IActions> getActions();
}

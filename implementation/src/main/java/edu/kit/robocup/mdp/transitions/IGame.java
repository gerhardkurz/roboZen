package edu.kit.robocup.mdp.transitions;

import java.util.List;

import edu.kit.robocup.game.State;
import edu.kit.robocup.game.action.IAction;

public interface IGame {
	// returns sequence of T states
	List<State> getStates();
	// returns sequence of T-1 states
	List<IAction> getActions();
}

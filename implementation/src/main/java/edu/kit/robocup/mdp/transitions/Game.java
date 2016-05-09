package edu.kit.robocup.mdp.transitions;

import java.util.List;

import edu.kit.robocup.game.State;
import edu.kit.robocup.game.action.IAction;

public class Game implements IGame {

	// states s_0 to s_T
	private List<State> states;
	
	// actions a_0 to a_(T-1), a_i is action between s_i and s_(i+1)
	private List<IAction> actions;
	
	@Override
	public List<State> getStates() {
		return this.states;
	}

	@Override
	public List<IAction> getActions() {
		return this.actions;
	}

}

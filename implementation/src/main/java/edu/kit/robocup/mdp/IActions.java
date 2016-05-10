package edu.kit.robocup.mdp;

import java.util.List;

import edu.kit.robocup.game.action.Action;

public interface IActions {
	List<Action> getActions();
	int getDimension();
}

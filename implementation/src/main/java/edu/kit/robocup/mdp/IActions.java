package edu.kit.robocup.mdp;

import java.util.List;

import edu.kit.robocup.game.action.IAction;

public interface IActions {
	List<IAction> getActions();
	int getDimension();
}

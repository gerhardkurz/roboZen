package edu.kit.robocup.mdp;

import edu.kit.robocup.game.Action;

import java.util.List;


public interface IActions {
	List<Action> getActions();
	int getDimension();
}

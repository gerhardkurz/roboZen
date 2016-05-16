package edu.kit.robocup.mdp;

import edu.kit.robocup.game.Action;
import edu.kit.robocup.game.IAction;

import java.util.List;


public interface IActionSet {
	List<IAction> getActions();
	int getDimension();
	// returns values for transitions in array
	double[] getArray();
	//returns coded types of action
	int[] getActionsType();
}

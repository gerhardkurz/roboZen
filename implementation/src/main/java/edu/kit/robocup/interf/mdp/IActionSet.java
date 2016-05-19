package edu.kit.robocup.interf.mdp;

import edu.kit.robocup.interf.game.IAction;

import java.util.List;


public interface IActionSet {
	List<IAction> getActions();
	int getDimension();
	// returns values for transition in array
	double[] getArray();
	//returns coded types of action
	int[] getActionsType();
}

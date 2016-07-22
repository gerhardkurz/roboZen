package edu.kit.robocup.interf.game;

import edu.kit.robocup.game.Action;

import java.io.Serializable;

public interface IAction extends Serializable {

	Action getActionType();
	
	//return number of parameters of Action
	int getActionDimension();
	
	//returns array of action
	double[] getArray();
}

package edu.kit.robozen.interf.game;

import edu.kit.robozen.game.Action;

import java.io.Serializable;

public interface IAction extends Serializable {

	Action getActionType();
	
	//return number of parameters of Action
	int getActionDimension();
	
	//returns array of action
	double[] getArray();
}

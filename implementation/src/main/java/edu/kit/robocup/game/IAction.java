package edu.kit.robocup.game;

public interface IAction {

	Action getActionType();
	
	//return number of parameters of Action
	int getActionDimension();
	
	//returns array of action
	double[] getArray();
}

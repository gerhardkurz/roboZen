package edu.kit.robocup.game;

public interface IAction {

	public Action getActionType();
	
	//return number of parameters of Action
	public int getActionDimension();
	
	//returns array of action
	public double[] getArray();
}

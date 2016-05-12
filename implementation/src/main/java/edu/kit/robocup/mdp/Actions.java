package edu.kit.robocup.mdp;

import java.util.List;

import edu.kit.robocup.game.Action;

public class Actions implements IActions {
	
	private List<Action> actions;
	private int dimension;

	public Actions(List<Action> actions, int dimension) {
		this.actions = actions;
		this.dimension = dimension;
	}
	
	public List<Action> getActions() {
		return this.actions;
	}

	public int getDimension() {
		return this.dimension;
	}

	public double[] getArray() {
		// TODO Auto-generated method stub
		return null;
	}

}

package edu.kit.robocup.game;

import java.io.Serializable;

public class Dash implements IAction, Serializable {

	private double power;
	
	public Dash(double power) {
		this.power = power;
	}
	
	@Override
	public Action getActionType() {
		return Action.dash;
	}

	@Override
	public int getActionDimension() {
		return 1;
	}

	@Override
	public double[] getArray() {
		return new double[]{power};
	}

	@Override
	public String toString() {
		return "Dash{" + power + "}";
	}


}

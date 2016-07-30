package edu.kit.robozen.game;

import edu.kit.robozen.interf.game.IAction;

import java.io.Serializable;

public class Dash implements IAction, Serializable {

	private int power;
	
	public Dash(int power) {
		this.power = power;
	}
	
	@Override
	public Action getActionType() {
		return Action.DASH;
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


	public int getPower() {
		return power;
	}
}

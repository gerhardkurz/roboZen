package edu.kit.robocup.game;

import java.io.Serializable;

public class Kick implements IAction, Serializable {

	private double power;
	private double direction;
	
	public Kick(double power, double direction) {
		this.power = power;
		this.direction = direction;
	}
	
	@Override
	public Action getActionType() {
		return Action.kick;
	}

	@Override
	public int getActionDimension() {
		return 2;
	}

	@Override
	public double[] getArray() {
		return new double[]{power,direction};
	}

	@Override
	public String toString() {
		return "Kick{" + power + ", " + direction + "}";
	}
}

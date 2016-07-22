package edu.kit.robocup.game;

import edu.kit.robocup.interf.game.IAction;

import java.io.Serializable;

public class Turn implements IAction, Serializable {

	private int angle;
	
	public Turn(int angle) {
		this.angle = angle;
	}
	
	@Override
	public Action getActionType() {
		return Action.TURN;
	}

	@Override
	public int getActionDimension() {
		return 1;
	}

	@Override
	public double[] getArray() {
		return new double[]{angle};
	}

	@Override
	public String toString() {
		return "Turn{" + angle + "}";
	}

	public int getAngle() {
		return angle;
	}
}

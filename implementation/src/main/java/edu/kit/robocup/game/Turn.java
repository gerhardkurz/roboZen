package edu.kit.robocup.game;

import java.io.Serializable;

public class Turn implements IAction, Serializable {

	private int moment;
	
	public Turn(int moment) {
		this.moment = moment;
	}
	
	@Override
	public Action getActionType() {
		return Action.turn;
	}

	@Override
	public int getActionDimension() {
		return 1;
	}

	@Override
	public double[] getArray() {
		return new double[]{moment};
	}

	@Override
	public String toString() {
		return "Turn{" + moment + "}";
	}
}

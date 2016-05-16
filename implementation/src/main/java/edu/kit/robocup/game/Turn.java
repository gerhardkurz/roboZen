package edu.kit.robocup.game;

import java.io.Serializable;

public class Turn implements IAction, Serializable {

	private int moment;
    private int angle;
	
	public Turn(int moment, int angle) {
		this.moment = moment;
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
		return new double[]{moment};
	}

	@Override
	public String toString() {
		return "Turn{" + moment + "}";
	}

	public int getMoment() {
		return moment;
	}

    public int getAngle() {
        return angle;
    }
}

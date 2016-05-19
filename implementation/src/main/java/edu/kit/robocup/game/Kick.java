package edu.kit.robocup.game;

import edu.kit.robocup.interf.game.IAction;

import java.io.Serializable;

public class Kick implements IAction, Serializable {

	private int power;
	private int direction;
	
	public Kick(int power, int direction) {
		this.power = power;
		this.direction = direction;
	}
	
	@Override
	public Action getActionType() {
		return Action.KICK;
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

    public int getPower() {
        return power;
    }

    public int getDirection() {
        return direction;
    }
}

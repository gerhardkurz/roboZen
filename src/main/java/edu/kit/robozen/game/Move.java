package edu.kit.robozen.game;

import edu.kit.robozen.interf.game.IAction;

import java.io.Serializable;

public class Move implements IAction, Serializable {

	private int x, y;
	
	public Move(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public Action getActionType() {
		return Action.MOVE;
	}

	@Override
	public int getActionDimension() {
		return 2;
	}

	@Override
	public double[] getArray() {
		return new double[]{x,y};
	}

	@Override
	public String toString() {
		return "Move{" + x + ", " + y + "}";
	}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

package edu.kit.robocup.game;

public class Move implements IAction {

	private int x, y;
	
	public Move(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public Action getActionType() {
		return Action.move;
	}

	@Override
	public int getActionDimension() {
		return 2;
	}

	@Override
	public double[] getArray() {
		return new double[]{x,y};
	}

}

package edu.kit.robocup.game;

public class Turn implements IAction {

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

}

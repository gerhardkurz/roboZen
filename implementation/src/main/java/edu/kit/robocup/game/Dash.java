package edu.kit.robocup.game;

public class Dash implements IAction {

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

}

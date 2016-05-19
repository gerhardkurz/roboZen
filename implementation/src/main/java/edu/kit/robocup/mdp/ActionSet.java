package edu.kit.robocup.mdp;

import java.util.List;

import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.interf.mdp.IActionSet;

public class ActionSet implements IActionSet {
	
	private List<IAction> actions;
	private int dimension;

	public ActionSet(List<IAction> actions) {
		this.actions = actions;
		int dim = 0;
		for (IAction action: actions) {
			dim += action.getActionDimension();
		}
		this.dimension = dim;
	}
	
	public List<IAction> getActions() {
		return this.actions;
	}

	public int getDimension() {
		return this.dimension;
	}

	public double[] getArray() {
		double[] a = new double[0];
		for (int i = 0; i < actions.size(); i++) {
			double[] act = actions.get(i).getArray();
			a = concat(a, act);
		}
		return a;
	}
	
	private double[] concat(double[] a, double[] b) {
		   int aLen = a.length;
		   int bLen = b.length;
		   double[] c= new double[aLen+bLen];
		   System.arraycopy(a, 0, c, 0, aLen);
		   System.arraycopy(b, 0, c, aLen, bLen);
		   return c;
		}

	@Override
	public int[] getActionsType() {
		int[] t = new int[actions.size()];
		for (int i = 0; i < actions.size(); i++) {
			t[i] = (actions.get(i).getActionType()).ordinal();
		}
		return t;
	}

	public String toString() {
		String result = "";
		for (int i = 0; i < actions.size(); i++) {
			result += actions.get(i).toString() + " ";
		}
		return result;
	}

}

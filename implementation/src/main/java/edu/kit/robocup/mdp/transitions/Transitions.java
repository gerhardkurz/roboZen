package edu.kit.robocup.mdp.transitions;

import java.util.List;

public class Transitions implements ITransitions {

	// m games of T sequences
	private List<IGame> games;
	private double[][] A, B;
	private double[] epsilon;
	
	//
	public Transitions(List<IGame> games) {
		this.games = games;
		int statedim = games.get(0).getStates().get(0).getDimension();
		int actiondim = games.get(0).getActions().get(0).getDimension();
		A = new double[statedim][statedim];
		B = new double[statedim][actiondim];
		epsilon = new double[statedim];
	}
	
	@Override
	public List<IGame> getGames() {
		return this.games;
	}

	@Override
	public void learn() {
		// TODO learn A, B, epsilon

	}

}

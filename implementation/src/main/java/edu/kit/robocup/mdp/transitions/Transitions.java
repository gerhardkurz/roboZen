package edu.kit.robocup.mdp.transitions;

import java.util.ArrayList;
import java.util.List;

public class Transitions implements ITransitions {

	// m games of T sequences
	private List<IGame> games;
	private double[][] A, B;
	private List<double[]> epsilon;
	
	//
	public Transitions(List<IGame> games) {
		this.games = games;
		int statedim = games.get(0).getStates().get(0).getDimension();
		int actiondim = games.get(0).getActions().get(0).getDimension();
		A = new double[statedim][statedim];
		B = new double[statedim][actiondim];
		epsilon = new ArrayList<double[]>(games.get(0).getStates().size());
	}
	
	@Override
	public List<IGame> getGames() {
		return this.games;
	}

	@Override
	public void learn() {
		// TODO learn A, B, epsilon
		int sum = 0;
		for (int i = 0; i < games.size(); i++) {
			IGame game = games.get(i);
			for (int t = 0; t < games.get(0).getStates().size() - 1; t++) {
				//sum += game.getStates().get(t+1) - (A*game.getStates().get(t) + B * game.getActions().get(t) + epsilon.get(t))
			}
		}
		// find argmin of sum
	}

}

package edu.kit.robocup.mdp.transitions;

import java.util.List;

public class Transitions implements ITransitions {

	// m games of T sequences
	private List<IGame> games;
	
	@Override
	public List<IGame> getGames() {
		return this.games;
	}

	@Override
	public void learn() {
		// TODO learn A, B, epsilon

	}

}

package edu.kit.robocup.mdp.transitions;

import java.util.List;

public interface ITransitions {
	// get sequences of m games
	List<IGame> getGames();
	
	// calculate A, B, epsilon
	void learn();
}

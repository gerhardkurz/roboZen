package edu.kit.robocup.mdp.transitions;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

public class Transitions implements ITransitions {

	// m games of T sequences, statesequence has to have the same length
	private List<IGame> games;
	private Matrix A, B;
	
	public Transitions(List<IGame> games) {
		this.games = games;
	}
	
	@Override
	public List<IGame> getGames() {
		return this.games;
	}

	@Override
	public void learn() {
		// TODO learn A, B
		int statedim = games.get(0).getStates().get(0).getDimension();
		int actiondim = games.get(0).getActions().get(0).getDimension();
		
		Matrix M;
		//T
		int gamelength = games.get(0).getGamelength();
		//m
		int numberOfGames = games.size();
		
		for (int m = 0; m < games.size(); m++) {
			for (int t = 0; t < gamelength; t++) {
				//x and y values are stacked
				Matrix S = getkDiagonalMatrix(games.get(m).getStates().get(t).getArray(), statedim);
				Matrix A;
			}
		}
		
		
		
		
		
		int sum = 0;
		for (int i = 0; i < games.size(); i++) {
			IGame game = games.get(i);
			for (int t = 0; t < games.get(0).getStates().size() - 1; t++) {
				//sum += game.getStates().get(t+1) - (A*game.getStates().get(t) + B * game.getActions().get(t) + epsilon.get(t))
			}
		}
		// find argmin of sum
	}
	
	/**
	 * returns matrix where array is diagonal entered
	 * @param value entered values
	 * @param size number of repeating value
	 * @return diagonal matrix
	 */
	private Matrix getkDiagonalMatrix(double[] value, int size) {
		double[][] values = new double[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size*value.length; j++) {
				if (i == j*value.length) {
					for (int k = 0; k < value.length; k++) {
						values[i][j+k] = value[k];
					}
					j = j + value.length;
				} else {
					values[i][j] = 0;
				}
			}
		}
		return new Matrix(values);
	}
	
	/**
	 * concat Matrix, so that C = (A B)
	 * @param a first matrix
	 * @param b second matrix
	 * @return concatenation
	 */
	private Matrix concatMatrix(Matrix a, Matrix b) {
		double[][] avalues = a.getArray();
		double[][] bvalues = b.getArray();
		double[][] cvalues = new double[avalues.length][avalues[0].length];
		if (avalues.length != bvalues.length) {
			throw new IllegalArgumentException("wrong dimensions of matrices");
		} else {
			for (int i = 0; i < avalues.length; i++) {
				for (int j = 0; j < avalues[0].length; j++) {
					cvalues[i][j] = avalues[i][j];
				}
			}
			for (int i = 0; i < bvalues.length; i++) {
				for (int j = 0; j < bvalues[0].length; j++) {
					cvalues[i + avalues.length][j] = bvalues[i][j];
				}
			}
		}
		return new Matrix(cvalues);
	}

}

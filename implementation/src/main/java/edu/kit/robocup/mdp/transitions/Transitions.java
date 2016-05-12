package edu.kit.robocup.mdp.transitions;

import java.util.List;
import java.util.Map;

import com.sun.corba.se.spi.ior.MakeImmutable;

import Jama.Matrix;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import edu.kit.robocup.mdp.IActions;

public class Transitions implements ITransitions {

	// m games of T sequences, statesequence has to have the same length
	private List<IGame> games;
	private DoubleMatrix2D A;
	/**
	 * contains matrices for all possible actions
	 */
	private Map<IActions, DoubleMatrix2D> B;
	
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
		
		// TODO how much action does exist? move, turn, dash kick?
		double numberactions = 4;
		double numberplayers = games.get(0).getNumberPlayers();
		double combinations = Math.pow(numberactions, numberplayers);
		
		
		//T
		int gamelength = games.get(0).getGamelength();
		//m
		int numberOfGames = games.size();
		
		DoubleFactory2D h = DoubleFactory2D.sparse;
		DoubleMatrix2D M = h.make(0, 0, 0);
		DoubleMatrix2D zero = h.make(statedim, actiondim, 0);
		for (int m = 0; m < games.size(); m++) {
			DoubleMatrix2D S = h.make(0, 0, 0);
			for (int t = 0; t < gamelength; t++) {
				//x and y values are stacked
				S = getkDiagonalMatrix(games.get(m).getStates().get(t).getArray(), statedim);
				int actualaction = 0; // TODO getActionIndex(games.get(m).getActions().get(t));
				for (int i = 0; i < combinations; i++) {
					if (actualaction == i) {
						DoubleMatrix2D action = getkDiagonalMatrix(games.get(m).getActions().get(t).getArray(), statedim);
						S = h.appendColumns(S, action);
					} else {
						S = h.appendColumns(S, zero);
					}
				}
				DoubleMatrix2D A = h.appendColumns(S, S);
			}
			M = h.appendRows(M, S);
		}
	}
	
	/**
	 * returns matrix where array is diagonal entered
	 * @param value entered values
	 * @param size number of repeating value
	 * @return diagonal matrix
	 */
	private DoubleMatrix2D getkDiagonalMatrix(double[] value, int size) {
		double[][] values = new double[size][size*value.length];
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
		DoubleFactory2D h = DoubleFactory2D.sparse;
		DoubleMatrix2D result = h.make(values);
		return result;
	}
	
	public static void main(String[] args) {
		DoubleFactory2D h = DoubleFactory2D.sparse;
		double[][] testarray = {{0, 0},{1, 2}};
		DoubleMatrix2D r = h.make(testarray);
		r = h.appendColumns(r, r);
		DoubleMatrix2D zero = h.make(5, 3, 0);
		System.out.println(r.toString());
		System.out.println(zero.toString());
	}
	
//	/**
//	 * concat Matrix, so that C = (A B)
//	 * @param a first matrix
//	 * @param b second matrix
//	 * @return concatenation
//	 */
//	private Matrix concatMatrix(Matrix a, Matrix b) {
//		double[][] avalues = a.getArray();
//		double[][] bvalues = b.getArray();
//		double[][] cvalues = new double[avalues.length][avalues[0].length];
//		if (avalues.length != bvalues.length) {
//			throw new IllegalArgumentException("wrong dimensions of matrices");
//		} else {
//			for (int i = 0; i < avalues.length; i++) {
//				for (int j = 0; j < avalues[0].length; j++) {
//					cvalues[i][j] = avalues[i][j];
//				}
//			}
//			for (int i = 0; i < bvalues.length; i++) {
//				for (int j = 0; j < bvalues[0].length; j++) {
//					cvalues[i + avalues.length][j] = bvalues[i][j];
//				}
//			}
//		}
//		return new Matrix(cvalues);
//	}

}

package edu.kit.robocup.mdp.transitions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.IPlayerState;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.mdp.IActions;

public class Transitions {

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
	
	// get sequences of m games
	public List<IGame> getGames() {
		return this.games;
	}

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
			}
			M = h.appendRows(M, S);
		}
		DoubleMatrix2D b = h.make(0, 0 ,0);
		for (int m = 0; m < games.size(); m++) {
			for (int t = 1; t < gamelength; t++) {
				double[] st = games.get(m).getStates().get(t).getArray();
				double[][] stst = new double[1][st.length];
				stst[0] = st;
				DoubleMatrix2D bb = h.make(stst);
				b = h.appendColumns(b, bb);
			}
		}
		Algebra a = new Algebra();
		DoubleMatrix2D ab = a.solve(M, b);
		double[] solution = ab.viewRow(0).toArray();
		DoubleFactory1D hh = DoubleFactory1D.sparse;
		DoubleMatrix1D d = hh.make(Arrays.copyOfRange(solution, 0, statedim*statedim));
		A = d.like2D(statedim, statedim);
		double[] solutionActions = Arrays.copyOfRange(solution, statedim*statedim + 1, solution.length);
		// TODO map solution matrices of size actiondim x statedim to B
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
				if (j == i*value.length) {
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
		double[] test1Darray = {0,1,2,3,4,5,6,5};
		DoubleMatrix2D zero = h.make(5, 3, 0);
		System.out.println(zero.toString());
		
		List<IGame> games = new ArrayList<IGame>();
		Ball ball = new Ball(10, 10);
		
		List<IPlayerState> players = new ArrayList<IPlayerState>();
		PlayerState p = new PlayerState("munich", 0, 5, 5);
		PlayerState p1 = new PlayerState("munich", 1, 4, 4);
		players.add(p);
		players.add(p1);
		
		Ball qball = new Ball(9, 9);
		
		List<IPlayerState> qplayers = new ArrayList<IPlayerState>();
		PlayerState qp = new PlayerState("munich", 0, 6, 6);
		PlayerState qp1 = new PlayerState("munich", 1, 3, 3);
		players.add(qp);
		players.add(qp1);

		List<State> states = new ArrayList<State>();
		State s = new State(ball, players);
		states.add(s);
		State qs = new State(qball, qplayers);
		states.add(qs);
		
		List<IActions> actions = new ArrayList<IActions>();
		int numberplayers = 2;
		Game g = new Game(states, actions, numberplayers);
		games.add(g);
		Transitions t = new Transitions(games);
		
		DoubleMatrix2D testing = t.getkDiagonalMatrix(test1Darray, 3);
		System.out.println(testing.toString());
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

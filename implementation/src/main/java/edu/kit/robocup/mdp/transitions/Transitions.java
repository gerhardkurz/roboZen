package edu.kit.robocup.mdp.transitions;


import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import edu.kit.robocup.game.Action;
import edu.kit.robocup.game.ActionFactory;
import edu.kit.robocup.game.Dash;
import edu.kit.robocup.game.IAction;
import edu.kit.robocup.game.Kick;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.IPlayerState;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.mdp.ActionSet;
import edu.kit.robocup.mdp.IActionSet;

public class Transitions {

    // m games of T sequences, statesequence has to have the same length
    private List<IGame> games;
    private DoubleMatrix2D A;
    /**
     * contains matrices for all possible actions
     */
    private DoubleMatrix2D[] B;

    public Transitions(List<IGame> games) {
        this.games = games;
        int numberOfCombinations = (int) Math.pow(games.get(0).getNumberPlayers(), Action.values().length);
        B = new DoubleMatrix2D[numberOfCombinations];
    }

    // get sequences of m games
    public List<IGame> getGames() {
        return this.games;
    }

    public void learn() {
        // TODO learn A, B
        int statedim = games.get(0).getStates().get(0).getDimension();
        System.out.println("Statedimension: " + statedim);

        int numberplayers = games.get(0).getNumberPlayers();
        // all possible combinations are numberofactions^numberofplayers
        double combinations = Math.pow(Action.values().length, numberplayers);


        //T
        int gamelength = games.get(0).getGamelength();
        System.out.println("Gamelength: " + gamelength);
        //m
        int numberOfGames = games.size();
        System.out.println("Gamesize: " + numberOfGames);

        DoubleFactory2D h = DoubleFactory2D.sparse;
        DoubleMatrix2D M = h.make(0, 0, 0);
        for (int m = 0; m < games.size(); m++) {
            for (int t = 0; t < gamelength - 1; t++) {
                DoubleMatrix2D S = getkDiagonalMatrix(games.get(m).getStates().get(t).getArray(), statedim);
                //System.out.println(S.toString());
                int actualaction = getActionIndex(games.get(m).getActions().get(t));
                //System.out.println(actualaction);
                for (int i = 0; i < combinations; i++) {
                    if (actualaction == i) {
                        DoubleMatrix2D action = getkDiagonalMatrix(games.get(m).getActions().get(t).getArray(), statedim);
                        S = h.appendColumns(S, action);
                    } else {
                        int actiondim = getDimension(i, numberplayers);
                        DoubleMatrix2D zero = h.make(statedim, actiondim * statedim, 0);
                        S = h.appendColumns(S, zero);
                        //System.out.println(S.toString());
                    }
                }
                if (M.size() == 0) {
                    M = S;
                } else {
                    M = h.appendRows(M, S);
                }
            }
        }

        // constructs b as vector / 1xn-Matrix of s_t+1 values
        DoubleMatrix2D b = h.make(0, 0, 0);
        for (int m = 0; m < games.size(); m++) {
            for (int t = 1; t < gamelength; t++) {
                double[] st = games.get(m).getStates().get(t).getArray();
                double[][] stst = new double[1][st.length];
                stst[0] = st;
                DoubleMatrix2D bb = h.make(stst);
                bb = bb.viewDice();
                if (b.size() == 0) {
                    b = bb;
                } else {
                    b = h.appendRows(b, bb);
                }
            }
        }

        // M*x = b is solved by x = (M^T*M)^-1 * M^T * b
        Algebra a = new Algebra();
        //DoubleMatrix2D factory = h.make(0, 0, 0);
        //System.out.println(M.toString());
        System.out.println(M.columns() + " " + M.rows());
        DoubleMatrix2D hasToInvert = a.mult(M.viewDice(), M);
        System.out.println("Rank of matrix that should be inverted is: " + a.rank(hasToInvert));
        System.out.println("Matrix has " + hasToInvert.columns() + " columns and " + hasToInvert.rows() + " rows");
        DoubleMatrix2D nearlyDone = a.mult(a.inverse(hasToInvert), M.viewDice());
        System.out.println("b has " + b.columns() + " columns and " + b.rows() + " rows");
        System.out.println("nearlyDone has " + nearlyDone.columns() + " columns and " + nearlyDone.rows() + " rows");

        DoubleMatrix2D ab = a.mult(nearlyDone, b);
        System.out.println("SolutionMatrix has " + ab.columns() + " columns and " + ab.rows() + " rows");
        double[] solution = ab.viewColumn(0).toArray();
        DoubleFactory1D hh = DoubleFactory1D.sparse;
        DoubleMatrix1D d = hh.make(Arrays.copyOfRange(solution, 0, statedim * statedim));
        A = d.like2D(statedim, statedim);
        for (int i = 0; i < statedim; i++) {
            for (int j = 0; j < statedim; j++) {
                A.set(i, j, d.get(i + j * statedim));
            }
        }
        System.out.println("A: " + A.toString());
        double[] solutionActions = Arrays.copyOfRange(solution, statedim * statedim, solution.length);
        for (int k = 0; k < B.length; k++) {
            int matrixsize = getDimension(k, numberplayers);
            d = hh.make(Arrays.copyOfRange(solutionActions, 0, matrixsize * statedim));
            B[k] = d.like2D(matrixsize, statedim);
            for (int i = 0; i < matrixsize; i++) {
                for (int j = 0; j < statedim; j++) {
                    B[k].set(i, j, d.get(i + j * matrixsize));
                }
            }
            System.out.println(B[k].toString());
            solutionActions = Arrays.copyOfRange(solutionActions, statedim * matrixsize, solutionActions.length);
        }
    }

    /**
     * recalculate actions from actionindex
     *
     * @param actionsIndex    index
     * @param numberOfPlayers basis of number system
     * @return dimension of the recalculated actions
     */
    private int getDimension(int actionsIndex, int numberOfPlayers) {
        int[] types = new int[numberOfPlayers];
        int rest = actionsIndex;
        for (int i = numberOfPlayers; i > 0; i--) {
            types[i - 1] = rest % (numberOfPlayers);
            rest = rest / (numberOfPlayers);
        }
        int dim = 0;
        ActionFactory a = new ActionFactory();
        for (int i = 0; i < numberOfPlayers; i++) {
            dim += a.getAction(types[i]).getActionDimension();
        }
        return dim;
    }

    /**
     * calculate actions in a n-dimensional numeral system, i-th player is the i-th position of number. n = numberOfPlayers/Actions - 1
     *
     * @param actions actions that are chosen
     * @return value in the n-dimensional numeral system
     */
    private int getActionIndex(IActionSet actions) {
        int[] types = actions.getActionsType();
        int n = actions.getActions().size();
        int codednumber = 0;
        for (int i = 0; i < types.length; i++) {
            codednumber += (int) Math.pow(n, i) * types[i];
        }
        return codednumber;
    }

    /**
     * returns matrix where array is diagonal entered
     *
     * @param value entered values
     * @param size  number of repeating value
     * @return diagonal matrix
     */
    private DoubleMatrix2D getkDiagonalMatrix(double[] value, int size) {
        double[][] values = new double[size][size * value.length];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size * value.length; j++) {
                if (j == i * value.length) {
                    for (int k = 0; k < value.length; k++) {
                        values[i][j + k] = value[k];
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
        double[][] testarray = {{0, 0}, {1, 2}};
        double[] test1Darray = {0, 1, 2, 3, 4, 5, 6, 5};
        DoubleMatrix2D zero = h.make(5, 3, 0);
        //System.out.println(zero.toString());

        List<IGame> games = new ArrayList<IGame>();

        int numberplayers = 2;
        Game g = new Game(getRandomStates(), getRandomActions(), numberplayers);
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions(), numberplayers);
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions(), numberplayers);
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions(), numberplayers);
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions(), numberplayers);
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions(), numberplayers);
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions(), numberplayers);
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions(), numberplayers);
        games.add(g);

        Transitions t = new Transitions(games);

        DoubleMatrix2D testing = t.getkDiagonalMatrix(test1Darray, 3);
        //System.out.println(t.getActionIndex(a));
        //System.out.println(t.getDimension(28, 4));
        //System.out.println(testing.toString());

        t.learn();
    }

    private static List<State> getRandomStates() {
        Ball ball = new Ball(Math.random(), Math.random(), Math.random(), Math.random());
        Ball ball1 = new Ball(Math.random(), Math.random(), Math.random(), Math.random());

        List<IPlayerState> players = new ArrayList<IPlayerState>();
        List<IPlayerState> players1 = new ArrayList<IPlayerState>();
        PlayerState p = new PlayerState("munich", 0, Math.random(), Math.random());
        PlayerState p1 = new PlayerState("munich", 1, Math.random(), Math.random());
        PlayerState p2 = new PlayerState("munich", 2, Math.random(), Math.random());
        PlayerState p3 = new PlayerState("munich", 3, Math.random(), Math.random());
        players.add(p);
        players.add(p1);
        players1.add(p2);
        players1.add(p3);

        Ball qball = new Ball(Math.random(), Math.random(), Math.random(), Math.random());
        Ball qball1 = new Ball(Math.random(), Math.random(), Math.random(), Math.random());

        List<IPlayerState> qplayers = new ArrayList<IPlayerState>();
        List<IPlayerState> qplayers1 = new ArrayList<IPlayerState>();
        PlayerState qp = new PlayerState("munich", 0, Math.random(), Math.random());
        PlayerState qp1 = new PlayerState("munich", 1, Math.random(), Math.random());
        PlayerState qp2 = new PlayerState("munich", 2, Math.random(), Math.random());
        PlayerState qp3 = new PlayerState("munich", 3, Math.random(), Math.random());
        qplayers.add(qp);
        qplayers.add(qp1);
        qplayers1.add(qp2);
        qplayers1.add(qp3);

        List<State> states = new ArrayList<State>();
        State s = new State(ball, players);
        State s1 = new State(ball1, players1);
        states.add(s);
        states.add(s1);
        State qs = new State(qball, qplayers);
        State qs1 = new State(qball1, qplayers1);
        states.add(qs);
        states.add(qs1);

        Ball qball11 = new Ball(Math.random(), Math.random(), Math.random(), Math.random());

        List<IPlayerState> qplayers11 = new ArrayList<IPlayerState>();
        PlayerState qp11 = new PlayerState("munich", 0, Math.random(), Math.random());
        PlayerState qp111 = new PlayerState("munich", 1, Math.random(), Math.random());
        qplayers11.add(qp11);
        qplayers11.add(qp111);
        State qs111 = new State(qball11, qplayers11);
        states.add(qs111);
        return states;
    }

    private static List<IActionSet> getRandomActions() {
        List<IActionSet> actions = new ArrayList<IActionSet>();
        IAction a0 = new Kick((int) Math.random(), (int) Math.random());
        IAction a1 = new Dash((int) Math.random());
        IAction a2 = new Dash((int) Math.random());
        IAction a3 = new Kick((int) Math.random(), (int) Math.random());
        IAction a4 = new Kick((int) Math.random(), (int) Math.random());
        IAction a5 = new Kick((int) Math.random(), (int) Math.random());
        IAction a6 = new Dash((int) Math.random());
        IAction a7 = new Dash((int) Math.random());
        List<IAction> helper = new ArrayList<IAction>();
        List<IAction> helper1 = new ArrayList<IAction>();
        List<IAction> helper2 = new ArrayList<IAction>();
        List<IAction> helper3 = new ArrayList<IAction>();
        helper.add(a0);
        helper.add(a1);
        helper1.add(a2);
        helper1.add(a3);
        helper2.add(a4);
        helper2.add(a5);
        helper3.add(a6);
        helper3.add(a7);
        ActionSet a = new ActionSet(helper);
        ActionSet a11 = new ActionSet(helper1);
        ActionSet a111 = new ActionSet(helper2);
        ActionSet a1111 = new ActionSet(helper3);
        actions.add(a);
        actions.add(a11);
        actions.add(a111);
        actions.add(a1111);
        return actions;
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

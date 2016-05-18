package edu.kit.robocup.mdp.transitions;


import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import edu.kit.robocup.Main;
import edu.kit.robocup.game.*;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.IPlayerState;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.mdp.ActionSet;
import edu.kit.robocup.mdp.IActionSet;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransitionsMap {

    static Logger logger = Logger.getLogger(Main.class.getName());

    // m games of T sequences, statesequence has to have the same length
    private List<GameMap> games;
    private DoubleMatrix2D A;
    /**
     * contains matrices for all possible actions
     */
    private DoubleMatrix2D[] B;

    public TransitionsMap(List<GameMap> games) {
        this.games = games;
        int numberOfCombinations = (int) Math.pow(games.get(0).getNumberPlayers(), Action.values().length);
        B = new DoubleMatrix2D[numberOfCombinations];
    }

    // get sequences of m games
    public List<GameMap> getGames() {
        return this.games;
    }

    public void learn() {
        // TODO learn A, B
        int statedim = games.get(0).getStates().get(0).getDimension();

        int numberplayers = games.get(0).getNumberPlayers();
        // all possible combinations are numberofactions^numberofplayers
        double combinations = Math.pow(Action.values().length, numberplayers);


        //T
        int gamelength = games.get(0).getGamelength();
        //m
        int numberOfGames = games.size();

        DoubleFactory2D h = DoubleFactory2D.sparse;
        DoubleMatrix2D M = h.make(0, 0, 0);
        for (int m = 0; m < games.size(); m++) {
            String team = games.get(m).getTeam();
            for (int t = 0; t < gamelength - 1; t++) {
                DoubleMatrix2D S = getkDiagonalMatrix(games.get(m).getStates().get(t).getArray(), statedim);
                State s = games.get(m).getStates().get(t);
                List<IAction> a = new ArrayList<>();
                for (int i = 0; i < s.getPlayers(games.get(m).getTeam()).size(); i++) {
                    a.add(games.get(m).getActions().get(t).get(s.getPlayers(team).get(i)) );
                }
                IActionSet actions = new ActionSet(a);
                int actualaction = getActionIndex(actions);
                for (int i = 0; i < combinations; i++) {
                    if (actualaction == i) {
                        DoubleMatrix2D action = getkDiagonalMatrix(actions.getArray(), statedim);
                        S = h.appendColumns(S, action);
                    } else {
                        int actiondim = getDimension(i, numberplayers);
                        DoubleMatrix2D zero = h.make(statedim, actiondim * statedim, 0);
                        S = h.appendColumns(S, zero);
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
        System.out.println(M.columns() + " " + M.rows());
        DoubleMatrix2D hasToInvert = a.mult(M.viewDice(), M);
        DoubleMatrix2D nearlyDone = a.mult(a.inverse(hasToInvert), M.viewDice());

        DoubleMatrix2D ab = a.mult(nearlyDone, b);
        double[] solution = ab.viewColumn(0).toArray();
        DoubleFactory1D hh = DoubleFactory1D.sparse;
        DoubleMatrix1D d = hh.make(Arrays.copyOfRange(solution, 0, statedim * statedim));
        A = d.like2D(statedim, statedim);
        for (int i = 0; i < statedim; i++) {
            for (int j = 0; j < statedim; j++) {
                A.set(i, j, d.get(i + j * statedim));
            }
        }
        logger.info("A: " + A.toString());
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
            logger.info(B[k].toString());
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
    }

}

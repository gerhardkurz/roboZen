package edu.kit.robocup.mdp.transition;


import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import edu.kit.robocup.Main;
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
import edu.kit.robocup.recorder.GameReader;
import org.apache.log4j.Logger;

public class Transitions {

    static Logger logger = Logger.getLogger(Main.class.getName());

    // m games of T sequences, statesequence has to have the same length
    private List<Game> games;
    private DoubleMatrix2D A;
    /**
     * contains matrices for all possible actions
     */
    private DoubleMatrix2D[] B;

    public Transitions(List<Game> games) {
        this.games = games;
        int numberOfCombinations = (int) Math.pow(games.get(0).getNumberPlayers(), Action.values().length);
        B = new DoubleMatrix2D[numberOfCombinations];
    }

    // get sequences of m games
    public List<Game> getGames() {
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

        DoubleFactory2D h = DoubleFactory2D.sparse;
        int dimensionsMrow = (gamelength - 1) * games.size() * statedim;
        int dimensionsMcol = statedim * games.get(0).getStates().get(0).getArray().length;
        for (int i = 0; i < combinations; i++) {
            dimensionsMcol += getDimension(i, numberplayers) * statedim;
        }
        DoubleMatrix2D M = h.make(dimensionsMrow, dimensionsMcol);
        logger.info("Matrix M has size " + dimensionsMrow + ", " + dimensionsMcol);
        for (int i = 0; i < B.length; i++) {
            B[i] = h.make(getDimension(i, numberplayers), statedim);
        }

        for (int m = 0; m < games.size(); m++) {
            for (int t = 0; t < gamelength - 1; t++) {
                double[] s = games.get(m).getStates().get(t).getArray();
                for (int j = 0; j < statedim; j++) {
                    for (int c = 0; c < s.length; c++) {
                        M.set(j + ((m*(gamelength-1) + t) * statedim), j*statedim + c, s[c]);
                    }
                }
                int actualaction = getActionIndex(games.get(m).getActions().get(t));
                int columnindex = statedim * statedim;
                for (int i = 0; i < combinations; i++) {
                    if (actualaction == i) {
                        double[] d = games.get(m).getActions().get(t).getArray();
                        for (int j = 0; j < statedim; j++) {
                            for (int c = 0; c < d.length; c++) {
                                M.set(j + ((m * (gamelength - 1) + t) * statedim), columnindex + j * d.length + c, d[c]);
                            }
                        }
                    }
                    columnindex += statedim * getDimension(i, numberplayers);
                }
            }
        }

        logger.info("M is created");

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

        logger.info("b is created");

        // M*x = b is solved by x = (M^T*M)^-1 * M^T * b
        Algebra a = new Algebra();
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

    public String test(){
        int numberplayers = games.get(0).getNumberPlayers();
        // all possible combinations are numberofactions^numberofplayers
        double combinations = Math.pow(Action.values().length, numberplayers);
        for (int i = 0; i < combinations; i++) {
            boolean found = false;
            for (int m = 0; m < games.size(); m++) {
                for (int k = 0; k < games.get(m).getActions().size(); k++) {
                    int index = getActionIndex(games.get(m).getActions().get(k));
                    if (index == i) {
                        found = true;
                    }
                }
            }
            if (found == false) {
                return "Error, Matrix will be singular because Actioncombination " + i +": " + getActions(i, numberplayers) + " doesn't exist!";
            }
        }
        return "All Actioncombinations are found!";

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
            types[i - 1] = rest % (Action.values().length);
            rest = rest / (Action.values().length);
        }
        int dim = 0;
        ActionFactory a = new ActionFactory();
        for (int i = 0; i < numberOfPlayers; i++) {
            dim += a.getAction(types[i]).getActionDimension();
        }
        return dim;
    }
    private ActionSet getActions(int actionsIndex, int numberOfPlayers) {
        int[] types = new int[numberOfPlayers];
        int rest = actionsIndex;
        for (int i = numberOfPlayers; i > 0; i--) {
            types[i - 1] = rest % (Action.values().length);
            rest = rest / (Action.values().length);
        }
        int dim = 0;
        ActionFactory a = new ActionFactory();
        List<IAction> l = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            l.add(a.getAction(types[i]));
        }
        ActionSet result = new ActionSet(l);
        return result;
    }


    /**
     * calculate actions in a n-dimensional numeral system, i-th player is the i-th position of number. n = numberOfPlayers/Actions - 1
     *
     * @param actions actions that are chosen
     * @return value in the n-dimensional numeral system
     */
    private int getActionIndex(IActionSet actions) {
        int[] types = actions.getActionsType();
        int n = Action.values().length;
        int codednumber = 0;
        for (int i = 0; i < types.length; i++) {
            codednumber += (int) Math.pow(n, i) * types[i];
        }
        return codednumber;
    }

    public static void main(String[] args) {
        DoubleFactory2D h = DoubleFactory2D.sparse;
        double[][] testarray = {{0, 0}, {1, 2}};
        double[] test1Darray = {0, 1, 2, 3, 4, 5, 6, 5};
        DoubleMatrix2D zero = h.make(5, 3, 0);

        List<Game> games = new ArrayList<Game>();

        GameReader r = new GameReader("allcombinations1000WithBall");
        games.add(r.getGameFromFile());

        /*int numberplayers = 2;
        Game g = new Game(getRandomStates(), getRandomActions());
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions());
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions());
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions());
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions());
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions());
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions());
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions());
        games.add(g);*/

        Transitions t = new Transitions(games);
        /*ActionFactory a = new ActionFactory();
        IAction ac = a.getAction(0);
        IAction ad = a.getAction(3);
        List<IAction> ent = new ArrayList<>();
        ent.add(ac);
        ent.add(ad);
        ActionSet ente = new ActionSet(ent);
        logger.info(t.getActionIndex(ente));*/
        logger.info(t.test());

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
        IAction a0 = new Kick((int) (Math.random() * 30), (int) (Math.random() * 30));
        IAction a1 = new Dash((int) (Math.random() * 30));
        IAction a2 = new Dash((int) (Math.random() * 30));
        IAction a3 = new Kick((int) (Math.random() * 30), (int) (Math.random() * 30));
        IAction a4 = new Kick((int) (Math.random() * 30), (int) (Math.random() * 30));
        IAction a5 = new Kick((int) (Math.random() * 30), (int) (Math.random() * 30));
        IAction a6 = new Dash((int) (Math.random() * 30));
        IAction a7 = new Dash((int) (Math.random() * 30));
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
}

package edu.kit.robocup.mdp.transition;


import Jama.Matrix;
import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import edu.kit.robocup.Main;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.Action;
import edu.kit.robocup.game.ActionFactory;
import edu.kit.robocup.game.Dash;
import edu.kit.robocup.game.Kick;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.interf.mdp.IActionSet;
import edu.kit.robocup.mdp.ActionSet;
import edu.kit.robocup.mdp.PlayerActionSet;
import edu.kit.robocup.recorder.GameReader;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.log4j.Logger;
import org.openimaj.math.matrix.PseudoInverse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Transitions {

    static Logger logger = Logger.getLogger(Transitions.class.getName());

    // m games of T sequences, statesequence has to have the same length
    private List<Game> games;

    private DoubleMatrix2D A;

    /**
     * contains matrices for all possible actions
     */
    private DoubleMatrix2D[] B;

    MultivariateNormalDistribution dist;

    public Transitions(List<Game> games) {
        this.games = games;
        int numberOfCombinations = (int) Math.pow(Action.values().length, games.get(0).getNumberPlayers());
        B = new DoubleMatrix2D[numberOfCombinations];
    }

    public DoubleMatrix2D getA() {
        return this.A;
    }

    public DoubleMatrix2D[] getB() {
        return this.B;
    }

    // get sequences of m games
    public List<Game> getGames() {
        return this.games;
    }

    public MultivariateNormalDistribution getDist() {
        return this.dist;
    }

    public void startLearning() {
        this.normalize();
        this.calculateCovarianceMatrix();
        logger.info(this.test());
        this.learn();
    }

    /**
     * reduce sequence of states and actions, so that every game has the same length
     */
    private void normalize() {
        int minState = 1000000000;
        int minAct = 1000000000;
        for (Game game : games) {
            if (minState > game.getStates().size()) {
                minState = game.getStates().size();
                minAct = game.getActions().size();
            }
        }
        logger.info("Games will be reduced so that they consists of " + minState + " states and " + minAct + " Actions");
        List<Game> newGames = new ArrayList<>();
        for (Game game : games) {
            List<State> s = new ArrayList<>();
            for (int j = 0; j < minState; j++) {
                s.add(game.getStates().get(j));
            }
            List<PlayerActionSet> a = new ArrayList<>();
            for (int j = 0; j < minAct; j++) {
                a.add(game.getActions().get(j));
            }
            newGames.add(new Game(s, a));
        }
        this.games = newGames;
    }

    /**
     * calculates covarianzmatrix of all states that are in the games
     */
    private void calculateCovarianceMatrix(){
        DoubleFactory2D h = DoubleFactory2D.sparse;
        DoubleMatrix2D covarianceMatrix = h.make(games.get(0).getStates().get(0).getDimension(), games.get(0).getStates().get(0).getDimension());
        int s = covarianceMatrix.columns();
        //logger.info("Statedimension is " + s);
        double[] mean = new double[s];
        for (int i = 0; i < s; i++) {
            double curmean = 0;
            double count = 0;
            for (Game game : games) {
                for (int t = 0; t < game.getGamelength(); t++) {
                    curmean += game.getStates().get(t).getArray()[i];
                    count++;
                }
            }
            mean[i] = curmean/count;
        }
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                double cov = 0;
                for (Game game : games) {
                    for (int t = 0; t < game.getGamelength(); t++) {
                        cov += (game.getStates().get(t).getArray()[i] - mean[i]) * (game.getStates().get(t).getArray()[j] - mean[j]);
                    }
                }
                cov = cov/(covarianceMatrix.columns() - 1);
                covarianceMatrix.set(i, j, cov);
            }
        }
        for (int i = 0; i < s; i++) {
            mean[i] = 0;
        }
        //logger.info("Covariance Matrix is " + covarianceMatrix.toString());
        dist = new MultivariateNormalDistribution(mean, covarianceMatrix.toArray());
    }

    /**
     * samples a resulting state when a specified action is taken in a specified state
     * @param s actual state
     * @param a actual chosen action
     * @param pitchSide side of the team
     * @return new state
     */
    public State getNewStateSample(State s, IActionSet a, PitchSide pitchSide) {
        int actionindex = getActionIndex(a);
        Algebra alg = new Algebra();
        DoubleFactory1D h = DoubleFactory1D.dense;
        //logger.info("A is size " + A.columns() + " , " + A.rows() + " and statedimension is " + s.getArray().length);
        DoubleMatrix1D state = h.make(s.getArray());
        DoubleMatrix1D calculationAs = h.make(s.getArray().length);
        A.zMult(state, calculationAs);
        DoubleMatrix1D action = h.make(a.getArray());
        DoubleMatrix1D calculationBa = h.make(s.getArray().length);
        B[actionindex].zMult(action, calculationBa);
        DoubleMatrix1D calculationEpsilon = h.make(dist.sample());
        DoubleMatrix1D result = h.make(s.getDimension());
        //logger.info("calculationAs " + calculationAs.size() + "calculationBa" + calculationBa.size() + "calculationEps" + calculationEpsilon.size());
        for (int i = 0; i < s.getDimension(); i++) {
            // result = A*s+B*a+epsilon
            result.set(i, calculationAs.get(i) + calculationBa.get(i) + calculationEpsilon.get(i));
        }
        return new State(result.toArray(), pitchSide);
    }

    /**
     * tests, whether all action combinations do exist. If they don't, then there will be thrown an exception in the learn method
     * there can still be thrown an exception, if a player or the ball doesn't move or just in one direction
     * @return output whether all actioncombinations are found or not
     */
    private String test(){
        int numberplayers = games.get(0).getNumberPlayers();
        // all possible combinations are numberofactions^numberofplayers
        double combinations = Math.pow(Action.values().length, numberplayers);
        for (int i = 0; i < combinations; i++) {
            boolean found = false;
            for (Game game : games) {
                for (int k = 0; k < game.getActions().size(); k++) {
                    int index = getActionIndex(null);//TODO game.getActions().get(k));
                    if (index == i) {
                        found = true;
                    }
                }
            }
            if (!found) {
                return "Error, Matrix will be singular because Actioncombination " + i +": " + getActions(i, numberplayers) + " doesn't exist!";
            }
        }
        return "All Actioncombinations are found!";

    }

    /**
     * calculates best Matrices A and B[i], so that s_t+1 = A*s_t + B[i]*ai_t
     */
    private void learn() {
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

        List<Information> info = new ArrayList<>();
        List<Integer> leftOut = new ArrayList<>();
        for (int m = 0; m < games.size(); m++) {
            for (int t = 0; t < gamelength - 1; t++) {
                Information cur = new Information(games.get(m).getStates().get(t), null);//TODO games.get(m).getActions().get(t));
                boolean doubleInformation = false;
                for (Information anInfo : info) {
                    if (anInfo.equals(cur)) {
                        doubleInformation = true;
                    }
                }
                if (doubleInformation) {
                    leftOut.add(m);
                    leftOut.add(t);
                } else {
                    info.add(cur);
                    double[] s = cur.getState().getArray();
                    for (int j = 0; j < statedim; j++) {
                        for (int c = 0; c < s.length; c++) {
                            M.set(j + ((m * (gamelength - 1) + t) * statedim) - (leftOut.size()/2 * statedim), j * statedim + c, s[c]);
                        }
                    }
                    int actualaction = getActionIndex(cur.getActionSet());
                    int columnindex = statedim * statedim;
                    for (int i = 0; i < combinations; i++) {
                        if (actualaction == i) {
                            double[] d = cur.getActionSet().getArray();
                            for (int j = 0; j < statedim; j++) {
                                for (int c = 0; c < d.length; c++) {
                                    M.set(j + ((m * (gamelength - 1) + t) * statedim) - (leftOut.size()/2 * statedim), columnindex + j * d.length + c, d[c]);
                                }
                            }
                        }
                        columnindex += statedim * getDimension(i, numberplayers);
                    }
                }
            }
        }
        DoubleMatrix2D MNew = h.make(dimensionsMrow-(leftOut.size()/2 * statedim), dimensionsMcol);
        for (int i = 0; i < MNew.rows(); i++) {
            for (int j = 0; j < MNew.columns(); j++) {
                MNew.set(i, j, M.get(i, j));
            }
        }
        logger.info("Double information found: " + leftOut.size()/2);
        logger.info("M is created");

        // constructs b as vector / 1xn-Matrix of s_t+1 values
        DoubleMatrix2D b = h.make(0, 0, 0);
        for (int m = 0; m < games.size(); m++) {
            for (int t = 1; t < gamelength; t++) {
                boolean doubleInformation = false;
                for (int i = 0; i < leftOut.size()/2; i++) {
                    if (leftOut.get(2*i) == m && leftOut.get(2*i+1) == t - 1) {
                        doubleInformation = true;
                    }
                }
                if (! doubleInformation) {
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
        }

        logger.info("b is created");

        // M*x = b is solved by x = (M^T*M)^-1 * M^T * b
        Algebra a = new Algebra();
        DoubleMatrix2D hasToInvert = a.mult(MNew.viewDice(), MNew);

        // convert to jama matrix as there a pseudoinverse can be calculated, converting back to colt
        Matrix invertable = new Matrix(hasToInvert.toArray());
        Matrix inverted = PseudoInverse.pseudoInverse(invertable);
        DoubleMatrix2D invertedByJama = h.make(inverted.getArray());

        //logger.info("Rank of matrix of size " + hasToInvert.rows() + " x " + hasToInvert.columns() + " is : " + a.rank(hasToInvert));
        DoubleMatrix2D nearlyDone = a.mult(invertedByJama, MNew.viewDice());

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
        logger.info("B will contain " + B.length + " Matrices.");
        for (int k = 0; k < B.length; k++) {
            int matrixsize = getDimension(k, numberplayers);
            d = hh.make(Arrays.copyOfRange(solutionActions, 0, matrixsize * statedim));
            B[k] = d.like2D(statedim, matrixsize);
            for (int i = 0; i < statedim; i++) {
                for (int j = 0; j < matrixsize; j++) {
                    B[k].set(i, j, d.get(i + j * statedim));
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

    /**
     * get actions of given actionindex
     * @param actionsIndex index of action combinations
     * @param numberOfPlayers number of players
     * @return actionset according to given actionindex
     */
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
        return new ActionSet(l);
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

    public static void main(String[] args) throws InterruptedException {
        List<Game> games = new ArrayList<Game>();
        /*GameReader r = new GameReader("allPlayersActionReduced");
        games.add(r.getGameFromFile());
        r = new GameReader("allActionCombinationsReducedPlayerStartingOnBall");
        games.add(r.getGameFromFile());*/

        DoubleFactory2D h = DoubleFactory2D.dense;

        int numberplayers = 2;
        /**Game g = new Game(getRandomStates(), getRandomActions());
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions());
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions());
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions());
        games.add(g);
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions());
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions());
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions());
        games.add(g);
        g = new Game(getRandomStates(), getRandomActions());
        games.add(g);*/

        //Transitions t = new Transitions(games);
        //t.startLearning();
        //ValueIteration v = new ValueIteration(t.getGames(), new Reward(200,-200,50, -50, 70, 170, -170, false ,"t1"));
    }

    private static List<State> getRandomStates() {
        Ball ball = new Ball(Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30);
        Ball ball1 = new Ball(Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30);

        List<IPlayerState> players = new ArrayList<IPlayerState>();
        List<IPlayerState> players1 = new ArrayList<IPlayerState>();
        PlayerState p = new PlayerState(PitchSide.WEST, 0, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30);
        PlayerState p1 = new PlayerState(PitchSide.WEST, 1, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30);
        PlayerState p2 = new PlayerState(PitchSide.WEST, 2, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30);
        PlayerState p3 = new PlayerState(PitchSide.WEST, 3, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30);
        players.add(p);
        players.add(p1);
        players1.add(p2);
        players1.add(p3);

        Ball qball =  new Ball(Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30);
        Ball qball1 = new Ball(Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30);

        List<IPlayerState> qplayers = new ArrayList<IPlayerState>();
        List<IPlayerState> qplayers1 = new ArrayList<IPlayerState>();
        PlayerState qp = new PlayerState(PitchSide.WEST, 0, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30);
        PlayerState qp1 = new PlayerState(PitchSide.WEST, 1, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30);
        PlayerState qp2 = new PlayerState(PitchSide.WEST, 2, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30);
        PlayerState qp3 = new PlayerState(PitchSide.WEST, 3, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30);
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

        Ball qball11 = new Ball(Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30);

        List<IPlayerState> qplayers11 = new ArrayList<IPlayerState>();
        PlayerState qp11 = new PlayerState(PitchSide.WEST, 0, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30);
        PlayerState qp111 = new PlayerState(PitchSide.WEST, 1, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30, Math.random() * 30);
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

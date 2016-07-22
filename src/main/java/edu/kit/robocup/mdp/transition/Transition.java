package edu.kit.robocup.mdp.transition;


import Jama.Matrix;
import cern.colt.function.DoubleDoubleFunction;
import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.*;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.mdp.PlayerActionSet;
import edu.kit.robocup.recorder.GameReader;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.log4j.Logger;
import org.openimaj.math.matrix.PseudoInverse;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Transition implements ITransition {

    static Logger logger = Logger.getLogger(Transition.class.getName());

    // m games of T sequences, statesequence has to have the same length
    private List<Game> games;

    private DoubleMatrix2D A;

    /**
     * contains matrices for all possible actions
     */
    private DoubleMatrix2D[] B;

    private enum Action{KICK,DASH,TURN};

    MultivariateNormalDistribution dist;

    public Transition(List<Game> games) {
        this.games = games;
        int numberOfCombinations = (int) Math.pow(Action.values().length, games.get(0).getNumberPlayers());
        A = null;
        B = new DoubleMatrix2D[numberOfCombinations];
    }

    @Override
    public boolean hasEnemyTeam() {
        return false;
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
    public State getNewStateSample(State s, PlayerActionSet a, PitchSide pitchSide) {
        int actionindex = getActionIndex(a);
        DoubleFactory1D h = DoubleFactory1D.dense;
        DoubleMatrix1D state = h.make(s.getArray());
        DoubleMatrix1D calculationAs = h.make(s.getArray().length);

        // A * state
        for (int i = 0; i < calculationAs.size(); i++) {
            calculationAs.set(i, A.viewColumn(i).zDotProduct(state));
        }
        DoubleMatrix1D action = h.make(a.getArray());
        DoubleMatrix1D calculationBa = h.make(s.getArray().length);
        // B[i] * state
        for (int i = 0; i < B[actionindex].columns(); i++) {
            calculationBa.set(i, B[actionindex].viewColumn(i).zDotProduct(state));
        }
        DoubleMatrix1D calculationEpsilon = h.make(dist.sample());
        //logger.info("Epsilon: " + calculationEpsilon.toString());
        DoubleMatrix1D result = h.make(s.getDimension());
        //double normEpsilon = 0;
        for (int i = 0; i < s.getDimension(); i++) {
            // result = A*s+B*a+epsilon
            result.set(i, calculationAs.get(i) + calculationBa.get(i)+ 0.005 * calculationEpsilon.get(i));
            //normEpsilon += 0.005 * Math.abs(calculationEpsilon.get(i));
        }
        //logger.info("Norm epsilon: " + normEpsilon);
        return new State(result.toArray(), pitchSide, s.getPlayers(pitchSide).size());
    }

    @Override
    public State getNewStateSampleWithEnemyPolicy(State s, PlayerActionSet a, PitchSide pitchSide) {
       return getNewStateSample(s, a, pitchSide);
    }

    @Override
    public int getNumberAllPlayers() {
        return games.get(0).getNumberofAllPlayers();
    }

    @Override
    public int getStateDimension() {
        return games.get(0).getStates().get(0).getDimension();
    }

    @Override
    public int getNumberPlayersPitchside() {
        return games.get(0).getActions().get(0).getActions().size();
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
                    int index = getActionIndex(game.getActions().get(k));
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
        logger.info("Calculating Transitions for " + games.size() + " games.");
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
                Information cur = new Information(games.get(m).getStates().get(t), games.get(m).getActions().get(t));
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

        //ogger.info("b " + b.columns() + " x " + b.rows());
        //logger.info("statedim " + games.get(0).getStates().get(0).getDimension());
        //logger.info("Mnew " + MNew.columns() + " x " + MNew.rows());


        logger.info("b has " + b.rows() + " rows.");

        // M*x = b is solved by x = (M^T*M)^-1 * M^T * b
        Algebra a = new Algebra();
        DoubleMatrix2D hasToInvert = a.mult(MNew.viewDice(), MNew);

        // convert to jama matrix as there a pseudoinverse can be calculated, converting back to colt
        Matrix invertable = new Matrix(hasToInvert.toArray());
        Matrix inverted = PseudoInverse.pseudoInverse(invertable);
        DoubleMatrix2D invertedByJama = h.make(inverted.getArray());

        logger.info("before inverting: " + hasToInvert.rows() + " x " + hasToInvert.columns() + " after inverting " + invertedByJama.rows() + " x " + invertedByJama.columns());

        //logger.info("Rank of matrix of size " + hasToInvert.rows() + " x " + hasToInvert.columns() + " is : " + a.rank(hasToInvert));
        DoubleMatrix2D nearlyDone = a.mult(invertedByJama, MNew.viewDice());
        logger.info("after nearlyDone: " + nearlyDone.rows() + " x " + nearlyDone.columns());
        DoubleMatrix2D ab = a.mult(nearlyDone, b);
        logger.info("done");

        //testing
        DoubleMatrix1D residuum = a.mult(MNew,ab.viewColumn(0));
        residuum = residuum.assign(b.viewColumn(0), new DoubleDoubleFunction() {
            @Override
            public double apply(double v, double v1) {
                return v-v1;
            }
        });
        logger.info("residuum: " + residuum.toString());

        double[] solution = ab.viewColumn(0).toArray();
        DoubleFactory1D hh = DoubleFactory1D.sparse;
        DoubleMatrix1D d = hh.make(Arrays.copyOfRange(solution, 0, statedim * statedim));
        logger.info("before A");
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

    public void setLearning(String filename) throws FileNotFoundException {
        this.normalize();
        this.calculateCovarianceMatrix();

        BufferedReader br;
        String zeile;
        try {
            DoubleFactory2D h = DoubleFactory2D.dense;
            FileReader fr = new FileReader(filename);
            br = new BufferedReader(fr);
            zeile = br.readLine();
            while(! (zeile.contains("A:"))) {
                zeile = br.readLine();
            }
            logger.info(zeile);
            String[] split = zeile.split(" ");
            A = h.make(Integer.parseInt(split[split.length - 4]), Integer.parseInt(split[split.length - 2]));
            for (int i = 0; i < Integer.parseInt(split[split.length - 4]); i++) {
                zeile = br.readLine();
                String[] sp = StringUtils.split(zeile);
                for (int j = 0; j < Integer.parseInt(split[split.length - 2]); j++) {
                    A.set(i, j, Double.parseDouble(sp[j]));
                }
            }
            br.readLine();
            for (int i = 0; i < B.length; i++) {
                zeile = br.readLine();
                String[] sp = zeile.split(" ");
                B[i] = h.make(Integer.parseInt(sp[sp.length - 4]), Integer.parseInt(sp[sp.length - 2]));
                for (int j = 0; j < Integer.parseInt(sp[sp.length - 4]); j++) {
                    zeile = br.readLine();
                    String[] s = StringUtils.split(zeile);
                    for (int k = 0; k < Integer.parseInt(sp[sp.length - 2]); k++) {
                        B[i].set(j, k, Double.parseDouble(s[k]));
                    }
                }
            }

            logger.info(A.toString());
            for (int i = 0; i < B.length; i++) {
                logger.info(B[i].toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
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
    private PlayerActionSet getActions(int actionsIndex, int numberOfPlayers) {
        int[] types = new int[numberOfPlayers];
        int rest = actionsIndex;
        for (int i = 0; i < numberOfPlayers; i++) {
            types[i] = rest % (Action.values().length);
            rest = rest / (Action.values().length);
        }
        int dim = 0;
        ActionFactory a = new ActionFactory();
        List<PlayerAction> l = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            l.add(new PlayerAction(i+1, a.getAction(types[i])));
        }
        return new PlayerActionSet(l);
    }


    /**
     * calculate actions in a n-dimensional numeral system, i-th player is the i-th position of number. n = numberOfPlayers/Actions - 1
     *
     * @param actions actions that are chosen
     * @return value in the n-dimensional numeral system
     */
    private int getActionIndex(PlayerActionSet actions) {
        int[] types = actions.getActionsType();
        int n = Action.values().length;
        int codednumber = 0;
        for (int i = 0; i < types.length; i++) {
            codednumber += (int) Math.pow(n, i) * types[i];
        }
        return codednumber;
    }

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        List<Game> games = GameReader.getGamesFromFiles("recordings/random300");
        Game part = new Game(games.get(0).getStates().subList(0,120), games.get(0).getActions().subList(0,119));
        games.add(part);
        /*r = new GameReader("allActionCombinationsReducedPlayerStartingOnBall");
        games.add(r.getGameFromFile());*/

        DoubleFactory2D h = DoubleFactory2D.dense;

        int numberplayers = 2;
        Transition t = new Transition(games);
        //t.startLearning();
        t.setLearning("C:/Users/dani/Documents/Praktikum/SS16_Robocup/lograndom300.txt");
        StateFactory f = new StateFactory();
        /*State s = f.getRandomState(t.getGames().get(0).getNumberofAllPlayers(), PitchSide.EAST);
        logger.info(s);
        PlayerActionSet a = t.getGames().get(0).getActions().get(5);
        logger.info(a);
        logger.info(t.getNewStateSample(s, a, PitchSide.EAST));

        logger.info(a);
        logger.info(t.getActionIndex(a));
        logger.info(t.getActions(t.getActionIndex(a), 2));

        a = t.getGames().get(0).getActions().get(1);
        logger.info(a);
        logger.info(t.getNewStateSample(s, a, PitchSide.EAST));

        logger.info(t.getNewStateSample(games.get(0).getStates().get(0), games.get(0).getActions().get(0), PitchSide.EAST));
        logger.info(games.get(0).getStates().get(1).toString());*/

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

        //Transition t = new Transition(games);
        //t.startLearning();
        //ValueIteration v = new ValueIteration(t.getGames(), new Reward(200,-200,50, -50, 70, 170, -170, false ,"t1"));
    }
}

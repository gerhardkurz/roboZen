package edu.kit.robocup.mdp;


import edu.kit.robocup.game.Dash;
import edu.kit.robocup.game.Kick;
import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.game.Turn;
import edu.kit.robocup.interf.game.IAction;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.javacc.parser.LexGen.actions;

/**
 * discretizes the actions and calculates a set of all possible discretized action vectors
 */
public class PlayerActionSetFactory {
    private static Logger logger = Logger.getLogger(PlayerActionSetFactory.class.getName());

    private final int maxTurn = 180;
    private final int maxKick = 100;
    private final int maxDash = 100;

    public PlayerActionSetFactory() {};

    public List<PlayerActionSet> getActionPermutations(int playerCount, int turnCount, int kickCount, int dashCount) {
        List<IAction> actions = getAllDiscretizedActions(turnCount, kickCount, dashCount);
        List<PlayerActionSet> permutations = permuteActions(playerCount, actions);
        logger.info("ActionSets created. Count: " + permutations.size());
        return permutations;
    }

    private List<PlayerActionSet> permuteActions(int playerCount, List<IAction> actions) {
        // TODO only 2 Players supported, implement recursion for more players
        assert playerCount==2;

        List<PlayerActionSet> permutations = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++) {
            for (int j = 0; j < actions.size(); j++) {
                permutations.add(createActionSet(i, j, actions));
            }
        }
        return permutations;
    }

    private PlayerActionSet createActionSet(int i, int j, List<IAction> actions) {
        List<PlayerAction> t = new ArrayList<>();
        t.add(new PlayerAction(1, actions.get(i)));
        t.add(new PlayerAction(2, actions.get(j)));
        return new PlayerActionSet(t);
    }

    private List<IAction> getAllDiscretizedActions(int turnCount, int kickCount, int dashCount) {
        ArrayList<IAction> actions = new ArrayList<>();
        actions.addAll(getDiscretizedTurn(turnCount));
        actions.addAll(getDiscretizedKick(kickCount));
        actions.addAll(getDiscretizedDash(dashCount));
        return actions;
    }

    private List<IAction> getDiscretizedTurn(int turnCount) {
        List<IAction> discretizedActions = new ArrayList<>();
        float stepSize = (maxTurn * 2) / turnCount;
        for (float i = -maxTurn; i <= maxTurn; i += stepSize) {
            discretizedActions.add(new Turn((int) i));
        }
        return discretizedActions;
    }

    private List<IAction> getDiscretizedKick(int kickCount) {
        List<IAction> discretizedActions = new ArrayList<>();
        float stepSize = maxKick / kickCount;
        for (float i = 0; i <= maxKick; i+= stepSize) {
            discretizedActions.add(new Kick((int) i, 0));
        }
        return discretizedActions;
    }

    private List<IAction> getDiscretizedDash(int dashCount) {
        List<IAction> discretizedActions = new ArrayList<>();
        float stepSize = maxDash / dashCount;
        for (float i = 0; i <= maxDash; i+= stepSize) {
            discretizedActions.add(new Dash((int) i));
        }
        return discretizedActions;
    }

    public List<PlayerActionSet> getReducedActions() {
        List<IAction> reducedActions = new ArrayList<>();
        reducedActions.add(new Turn(1));
        reducedActions.add(new Turn(5));
        reducedActions.add(new Turn(10));
        reducedActions.add(new Turn(25));
        reducedActions.add(new Turn(50));
        reducedActions.add(new Turn(-1));
        reducedActions.add(new Turn(-5));
        reducedActions.add(new Turn(-10));
        reducedActions.add(new Turn(-25));
        reducedActions.add(new Turn(-50));
        reducedActions.add(new Dash(40));
        reducedActions.add(new Kick(30, 0));
        reducedActions.add(new Kick(30, 25));
        reducedActions.add(new Kick(30, -25));

        /*for (int i = 0; i < 10; i++) {
            reducedActions.add(new Turn(i));
            reducedActions.add(new Turn(-i));
            reducedActions.add(new Kick(30, i));
            reducedActions.add(new Kick(30, -i));
        }
        for (int i = 20; i < 180; i = i+40) {
            reducedActions.add(new Turn(i));
            reducedActions.add(new Turn(-i));
            reducedActions.add(new Kick(30,i));
            reducedActions.add(new Kick(30, -i));
        }
        for (int i = 1; i < 100; i = 10+ i) {
            reducedActions.add(new Dash(i));
        }*/
        List<PlayerActionSet> permutations = new ArrayList<>();
        for (int i = 0; i < reducedActions.size(); i++) {
            for (int j = 0; j < reducedActions.size(); j++) {
                permutations.add(createActionSet(i, j, reducedActions));
            }
        }
        return permutations;
    }
}

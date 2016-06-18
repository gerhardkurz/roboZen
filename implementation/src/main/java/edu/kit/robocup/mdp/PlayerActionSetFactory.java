package edu.kit.robocup.mdp;


import edu.kit.robocup.game.Dash;
import edu.kit.robocup.game.Kick;
import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.game.Turn;
import edu.kit.robocup.interf.game.IAction;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * discretizes the actions and calculates a set of all possible discretized action vectors
 */
public class PlayerActionSetFactory {
    private static Logger logger = Logger.getLogger(PlayerActionSetFactory.class.getName());

    private int[] valuesTurn = new int[]{-180, -170, -160, -150, -140, -130, -120, -110, -100, -90, -80, -70, -60, -50, -40, -30, -20, -10, 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180};
    private int[] valuesKickPower = new int[]{-100, -90, -80, -70, -60, -50, -40, -30, -20, -10, 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
    private int[] valuesDash = new int[]{0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};

    public PlayerActionSetFactory() {};

    public List<PlayerActionSet> getActionAction(int indexFirst, int indexSecond) {
        List<IAction> first = getDiscretizedActions(indexFirst);
        List<IAction> second = getDiscretizedActions(indexSecond);

        List<PlayerActionSet> permutations = new ArrayList<>();

        // TODO hardgecoded for 2 players
        for (int i = 0; i < first.size(); i++) {
            for (int j = 0; j < second.size(); j++) {
                List<PlayerAction> t = new ArrayList<>();
                t.add(new PlayerAction(1, first.get(i)));
                t.add(new PlayerAction(2, second.get(j)));
                permutations.add(new PlayerActionSet(t));
            }
        }
        return permutations;
    }

    private List<IAction> getDiscretizedActions(int index) {
        switch (index) {
            case 0: {
                return getDiscretizedKick();
            }
            case 1: {
                return getDiscretizedDash();
            }
            case 2: {
                return getDiscretizedTurn();
            }
            default: {
                logger.error("Not the correct index of action");
                return null;
            }
        }
    }

    private List<IAction> getDiscretizedTurn() {
        List<IAction> discretizedActions = new ArrayList<>();

        for (int i = 0; i < valuesTurn.length; i++) {
            discretizedActions.add(new Turn(valuesTurn[i]));
        }
        return discretizedActions;
    }

    private List<IAction> getDiscretizedKick() {
        List<IAction> discretizedActions = new ArrayList<>();

        for (int i = 0; i < valuesTurn.length; i++) {
            for (int j = 0; j < valuesKickPower.length; j++) {
                discretizedActions.add(new Kick(valuesKickPower[j], valuesTurn[i]));
            }
        }

        return discretizedActions;
    }

    private List<IAction> getDiscretizedDash() {
        List<IAction> discretizedActions = new ArrayList<>();

        for (int i = 0; i < valuesDash.length; i++) {
            discretizedActions.add(new Dash(valuesDash[i]));
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
        List<PlayerActionSet> permutations = new ArrayList<>();
        for (int i = 0; i < reducedActions.size(); i++) {
            for (int j = 0; j < reducedActions.size(); j++) {
                List<PlayerAction> t = new ArrayList<>();
                t.add(new PlayerAction(1, reducedActions.get(i)));
                t.add(new PlayerAction(2, reducedActions.get(j)));
                permutations.add(new PlayerActionSet(t));
            }
        }
        return permutations;
    }
}

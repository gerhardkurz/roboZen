package edu.kit.robocup.mdp;


import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.Dash;
import edu.kit.robocup.game.Kick;
import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.game.Turn;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.interf.mdp.IState;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
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
        return permutations;
    }

    public List<PlayerActionSet> getActionPermutationsWithAngles(int playerCount, int turnCount, int kickCount, int dashCount, IState state, PitchSide pitchSide) {
        List<IAction> actions = getAllDiscretizedActions(turnCount, kickCount, dashCount);
        List<PlayerActionSet> permutations = permuteActionsWithAngle(playerCount, actions, state, pitchSide);
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

    private List<PlayerActionSet> permuteActionsWithAngle(int playerCount, List<IAction> actions, IState state, PitchSide pitchSide) {
        // TODO only 2 Players supported, implement recursion for more players
        assert playerCount==2;
        List<IAction> firstPlayer = actions;
        List<IAction> secondPlayer = actions;
        firstPlayer.add(new Turn((int) state.getPlayers(pitchSide).get(0).getAngleTo(state.getBall())));
        secondPlayer.add(new Turn((int) state.getPlayers(pitchSide).get(1).getAngleTo(state.getBall())));

        if (pitchSide == PitchSide.EAST) {
            firstPlayer.add(new Kick(100, (int) (state.getPlayers(pitchSide).get(0).getAngleTo(Constants.GOAL_WEST))));// - state.getPlayers(pitchSide).get(0).getBodyAngle())));
            secondPlayer.add(new Kick(100, (int) (state.getPlayers(pitchSide).get(1).getAngleTo(Constants.GOAL_WEST))));// - state.getPlayers(pitchSide).get(1).getBodyAngle())));
        } else {
            firstPlayer.add(new Kick(100, (int) (state.getPlayers(pitchSide).get(0).getAngleTo(Constants.GOAL_EAST))));// - state.getPlayers(pitchSide).get(0).getBodyAngle())));
            secondPlayer.add(new Kick(100, (int) (state.getPlayers(pitchSide).get(1).getAngleTo(Constants.GOAL_EAST))));// - state.getPlayers(pitchSide).get(1).getBodyAngle())));
        }

        List<PlayerActionSet> permutations = new ArrayList<>();
        for (int i = 0; i < firstPlayer.size(); i++) {
            for (int j = 0; j < secondPlayer.size(); j++) {
                permutations.add(createActionSet(i, j, firstPlayer, secondPlayer));
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

    private PlayerActionSet createActionSet(int i, int j, List<IAction> first, List<IAction> second) {
        List<PlayerAction> t = new ArrayList<>();
        t.add(new PlayerAction(1, first.get(i)));
        t.add(new PlayerAction(2, second.get(j)));
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
            if (i != 0) {
                discretizedActions.add(new Turn((int) i));
            }
        }
        return discretizedActions;
    }

    private List<IAction> getDiscretizedKick(int kickCount) {
        List<IAction> discretizedActions = new ArrayList<>();
        float stepSize = maxKick / kickCount;
        for (float i = stepSize; i <= maxKick; i+= stepSize) {
            discretizedActions.add(new Kick((int) i, 0));
        }
        return discretizedActions;
    }

    private List<IAction> getDiscretizedDash(int dashCount) {
        List<IAction> discretizedActions = new ArrayList<>();
        float stepSize = maxDash / dashCount;
        for (float i = stepSize; i <= maxDash; i+= stepSize) {
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

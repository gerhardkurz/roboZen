package edu.kit.robocup.mdp;


import edu.kit.robocup.game.Dash;
import edu.kit.robocup.game.Kick;
import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.game.Turn;
import edu.kit.robocup.interf.game.IAction;

import java.util.ArrayList;
import java.util.List;

/**
 * discretizes the actions and calculates a set of all possible discretized action vectors
 */
public class PlayerActionSetFactory {

    private int[] valuesTurn = new int[]{-180, -170, -160, -150, -140, -130, -120, -110, -100, -90, -80, -70, -60, -50, -40, -30, -20, -10, 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180};
    private int[] valuesKickPower = new int[]{-100, -90, -80, -70, -60, -50, -40, -30, -20, -10, 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
    private int[] valuesDash = new int[]{0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};

    public PlayerActionSetFactory() {};
    
    public List<PlayerActionSet> getSet() {
        List<IAction> discretizedActions = getDiscretizedActions();

        List<PlayerActionSet> permutations = new ArrayList<>();

        // TODO hardgecoded for 2 players
        for (int i = 0; i < discretizedActions.size(); i++) {
            for (int j = 0; j < discretizedActions.size(); j++) {
                List<PlayerAction> t = new ArrayList<>();
                t.add(new PlayerAction(1, discretizedActions.get(i)));
                t.add(new PlayerAction(2, discretizedActions.get(j)));
                permutations.add(new PlayerActionSet(t));
            }
        }
        return permutations;
    }

    private List<IAction> getDiscretizedActions() {
        List<IAction> discretizedActions = new ArrayList<>();

        for (int i = 0; i < valuesTurn.length; i++) {
            discretizedActions.add(new Turn(valuesTurn[i]));
        }
        for (int i = 0; i < valuesTurn.length; i++) {
            for (int j = 0; j < valuesKickPower.length; j++) {
                discretizedActions.add(new Kick(valuesKickPower[j], valuesTurn[i]));
            }
        }
        for (int i = 0; i < valuesDash.length; i++) {
            discretizedActions.add(new Dash(valuesDash[i]));
        }
        return discretizedActions;
    }

    public static void main(String[] args) {
        PlayerActionSetFactory a = new PlayerActionSetFactory();
        List<PlayerActionSet> s = a.getSet();
        System.out.println(s.toString());
    }
}

package edu.kit.robocup.mdp;

import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.interf.mdp.IActionSet;

import java.util.List;

/**
 * Created by dani on 02.06.2016.
 */
public class SimpleReward {

    private String teamname;
    private int goal;
    private boolean isTeamEast;

    public SimpleReward(int goal, boolean isTeamEast, String teamname) {
        this.teamname = teamname;
        this.goal = goal;
        this.isTeamEast = isTeamEast;
    }

    public int calculateReward(State prevState, IActionSet action, State nextState) {
        int reward = 0;
        Ball bprev = prevState.getBall();
        Ball bnext = nextState.getBall();

        if (isTeamEast) {
            if (Constants.GOAL_WEST.getDistance(bnext) == 0) {
                reward += goal;
            }
        } else {
            if (Constants.GOAL_EAST.getDistance(bnext) == 0) {
                reward += goal;
            }
        }

        return reward;
    }

    public String getTeamname() {
        return teamname;
    }

}

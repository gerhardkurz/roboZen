package edu.kit.robocup.mdp;

import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.interf.mdp.IActionSet;
import edu.kit.robocup.interf.mdp.IReward;

import java.util.List;

/**
 * Created by dani on 02.06.2016.
 */
public class SimpleReward implements IReward {

    private String teamname;
    private int goal;
    private PitchSide pitchSide;

    public SimpleReward(int goal, PitchSide pitchSide, String teamname) {
        this.teamname = teamname;
        this.goal = goal;
        this.pitchSide = pitchSide;
    }

    public int calculateReward(State prevState, PlayerActionSet action, State nextState) {
        int reward = 0;
        Ball bprev = prevState.getBall();
        Ball bnext = nextState.getBall();

        if (pitchSide.equals(PitchSide.EAST)) {
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

    @Override
    public PitchSide getPitchSide() {
        return pitchSide;
    }

    public String getTeamname() {
        return teamname;
    }

}

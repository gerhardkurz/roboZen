package edu.kit.robocup.mdp;

import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.Kick;
import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.interf.mdp.IReward;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dani on 02.06.2016.
 */
public class SimpleReward implements IReward {


    static Logger logger = Logger.getLogger(SimpleReward.class.getName());

    private int goal;
    private PitchSide pitchSide;

    public SimpleReward(int goal, PitchSide pitchSide) {
        this.goal = goal;
        this.pitchSide = pitchSide;
    }

    public double calculateReward(State prevState, PlayerActionSet action, State nextState) {
        double reward = 0;
        Ball bnext = nextState.getBall();

        if (pitchSide.equals(PitchSide.EAST)) {
            if (bnext.getPositionX() <= Constants.GOAL_WEST.getLowerPost().getPositionX() && bnext.getPositionY() <= Constants.GOAL_WEST.getUpperPost().getPositionY() && bnext.getPositionY() >= Constants.GOAL_WEST.getLowerPost().getPositionY()) {
                reward += goal;
            }
        } else {
            if (bnext.getPositionX() >= Constants.GOAL_EAST.getLowerPost().getPositionX() && bnext.getPositionY() <= Constants.GOAL_EAST.getUpperPost().getPositionY() && bnext.getPositionY() >= Constants.GOAL_EAST.getLowerPost().getPositionY()) {
                reward += goal;
            }
        }

        return reward;
    }

    @Override
    public PitchSide getPitchSide() {
        return pitchSide;
    }

    public static void main(String[] args){
        List<IPlayerState> p = new ArrayList<>();
        p.add(new PlayerState(PitchSide.EAST, 1, 0, 0, 2, 1, 22.5, 0));
        p.add(new PlayerState(PitchSide.EAST, 2, 0, 0));
        p.add(new PlayerState(PitchSide.WEST, 1, 0, 0));
        p.add(new PlayerState(PitchSide.WEST, 2, 0, 0));
        State s = new State(new Ball(-50, 0), p);

        SimpleReward r = new SimpleReward(2000, PitchSide.EAST);

        List<PlayerAction> l = new ArrayList<>();
        l.add(new PlayerAction(1, new Kick(0, 0)));
        l.add(new PlayerAction(2, new Kick(0, 0)));
        PlayerActionSet a = new PlayerActionSet(l);

        p = new ArrayList<>();
        p.add(new PlayerState(PitchSide.EAST, 1, -10, 0));
        p.add(new PlayerState(PitchSide.EAST, 2, -20, 0));
        p.add(new PlayerState(PitchSide.WEST, 1, 0, 0));
        p.add(new PlayerState(PitchSide.WEST, 2, 0, 0));
        State s1 = new State(new Ball(-70, 0), p);

        logger.info(r.calculateReward(s, a, s1));
    }

}

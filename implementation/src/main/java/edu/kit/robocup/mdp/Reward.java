package edu.kit.robocup.mdp;

import java.util.List;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.mdp.IActionSet;
import edu.kit.robocup.interf.mdp.IReward;

public class Reward implements IReward {
	
	private int goal;
	private int advGoal;
	private int gettingNearBall;
	private int gettingAwayFromBall;
	private int havingBall;
	private int gettingNearGoal;
	private int gettingAwayFromGoal;
    private PitchSide pitchSide;
	
	/**
	 * suggestion: Reward(200,-200,50, -50, 70, 170, -170)
	 * gettingNearBall gives +5 if distance to ball is 10, +50 if distance to ball is 1
	 * lineary add rewards: reward += gettingNear...*1/distance
	 * gettingNearGoal gives +17 if distance to goal is 10, +170 if distance to goal is 1
	 * 
	 * 
	 * all rewards will be added, so use negative rewards
	 * @param goal if ball is in own goal +goal
	 * @param advGoal if ball is in adversary goal +advGoal
	 * @param gettingNearBall for each player who gets nearer to ball +gettingNearBall
	 * @param gettingAwayFromBall for each player who gets farer away of the ball +gettingAwayFromBall
	 * @param havingBall if one player is in kickable margin +havingBall
	 * @param gettingNearGoal if ball gets nearer to adversary goal +gettingNearGoal
	 * @param gettingAwayFromGoal if ball gets farer away of the adversary goal +gettingAwayFromGoal
	 */
	public Reward(int goal, int advGoal, int gettingNearBall, int gettingAwayFromBall, int havingBall, int gettingNearGoal, int gettingAwayFromGoal, PitchSide pitchSide) {
		this.goal = goal;
		this.advGoal = advGoal;
		this.gettingAwayFromBall = gettingAwayFromBall;
		this.gettingNearBall = gettingNearBall;
		this.havingBall = havingBall;
		this.gettingNearGoal = gettingNearGoal;
		this.gettingAwayFromGoal = gettingAwayFromGoal;
        this.pitchSide = pitchSide;
	}

	public int calculateReward(State prevState, PlayerActionSet action, State nextState) {
		int reward = 0;
		List<IPlayerState> pprev = prevState.getPlayers(pitchSide);
		Ball bprev = prevState.getBall();
		List<IPlayerState> pnext = nextState.getPlayers(pitchSide);
		Ball bnext = nextState.getBall();
		for (int i = 0; i < pprev.size(); i++) {
			double distBallprev = bprev.getDistance(pprev.get(i));
			double distBallnext = bnext.getDistance(pnext.get(i));
			if (distBallnext > distBallprev) {
				reward += gettingAwayFromBall * 1/distBallnext;
			} else {
				reward += gettingNearBall * 1/distBallnext;
			}
			if (distBallnext <= Constants.KICKABLE_MARGIN) {
				reward += havingBall;
			}
			
		}

		if (pitchSide.equals(PitchSide.EAST)) {
			if (Constants.GOAL_WEST.getDistance(bnext) == 0) {
				reward += goal;
			}
			if (Constants.GOAL_EAST.getDistance(bnext) == 0) {
				reward += advGoal;
			}

			double distBallAdvGoalprev = Constants.GOAL_WEST.getDistance(bprev) ;

			double distBallAdvGoalnext = Constants.GOAL_WEST.getDistance(bnext);
			
			if (distBallAdvGoalnext < distBallAdvGoalprev) {
				reward += gettingNearGoal * 1/distBallAdvGoalnext;
			} else {
				reward += gettingAwayFromGoal * 1/distBallAdvGoalnext;
			}
		} else {
			if (Constants.GOAL_EAST.getDistance(bnext) == 0) {
				reward += goal;
			}
			if (Constants.GOAL_WEST.getDistance(bnext) == 0) {
				reward += advGoal;
			}

			double distBallAdvGoalprev = Constants.GOAL_EAST.getDistance(bprev) ;

			double distBallAdvGoalnext = Constants.GOAL_EAST.getDistance(bnext);

			if (distBallAdvGoalnext < distBallAdvGoalprev) {
				reward += gettingNearGoal * 1/distBallAdvGoalnext;
			} else {
				reward += gettingAwayFromGoal * 1/distBallAdvGoalnext;
			}
		}
		
		return reward;
	}

	public PitchSide getPitchSide() {
		return pitchSide;
	}
}

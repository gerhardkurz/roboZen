package edu.kit.robocup.mdp;

import java.util.ArrayList;
import java.util.List;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import edu.kit.robocup.constant.Constants;
import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.Kick;
import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.interf.game.IPlayer;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.mdp.IReward;
import org.apache.log4j.Logger;

public class Reward implements IReward {

	static Logger logger = Logger.getLogger(Reward.class.getName());
	
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

	public double calculateReward(State prevState, PlayerActionSet action, State nextState) {
		double reward = 0;
		List<IPlayerState> pprev = prevState.getPlayers(pitchSide);
		Ball bprev = prevState.getBall();
		List<IPlayerState> pnext = nextState.getPlayers(pitchSide);
		Ball bnext = nextState.getBall();
		for (int i = 0; i < pprev.size(); i++) {
			double distBallprev = bprev.getDistance(pprev.get(i));
			double distBallnext = bnext.getDistance(pnext.get(i));
			if (distBallnext > distBallprev) {
				if (distBallnext < 1) {
					reward += gettingAwayFromBall;
				} else {
					reward += gettingAwayFromBall * 1 / distBallnext;
				}
			} else {
				if (distBallnext != distBallprev) {
					if (distBallnext < 1) {
						reward += gettingNearBall;
					} else {
						reward += gettingNearBall * 1 / distBallnext;
					}
				}
			}
			if (distBallnext <= Constants.KICKABLE_MARGIN) {
				reward += havingBall;
			}
			
		}

		if (pitchSide.equals(PitchSide.EAST)) {
			if (bnext.getPositionX() <= Constants.GOAL_WEST.getLowerPost().getPositionX() && bnext.getPositionY() <= Constants.GOAL_WEST.getUpperPost().getPositionY() && bnext.getPositionY() >= Constants.GOAL_WEST.getLowerPost().getPositionY()) {
				reward += goal;
				//logger.info("goal");
			}
			if (bnext.getPositionX() >= Constants.GOAL_EAST.getLowerPost().getPositionX() && bnext.getPositionY() <= Constants.GOAL_EAST.getUpperPost().getPositionY() && bnext.getPositionY() >= Constants.GOAL_EAST.getLowerPost().getPositionY()) {
				reward += advGoal;
			}

			double distBallAdvGoalprev = Constants.GOAL_WEST.getDistance(bprev) ;

			double distBallAdvGoalnext = Constants.GOAL_WEST.getDistance(bnext);
			if (distBallAdvGoalnext < distBallAdvGoalprev) {
				if (distBallAdvGoalnext < 1) {
					reward += gettingNearGoal;
				} else {
					reward += gettingNearGoal * 1 / distBallAdvGoalnext;
				}
			} else {
				if (distBallAdvGoalnext != distBallAdvGoalprev) {
					if (distBallAdvGoalnext < 1) {
						reward += gettingAwayFromBall;
					} else {
						reward += gettingAwayFromGoal * 1 / distBallAdvGoalnext;
					}
				}
			}
		} else {
			if (bnext.getPositionX() <= Constants.GOAL_WEST.getLowerPost().getPositionX() && bnext.getPositionY() <= Constants.GOAL_WEST.getUpperPost().getPositionY() && bnext.getPositionY() >= Constants.GOAL_WEST.getLowerPost().getPositionY()) {
				reward += advGoal;
			}
			if (bnext.getPositionX() >= Constants.GOAL_EAST.getLowerPost().getPositionX() && bnext.getPositionY() <= Constants.GOAL_EAST.getUpperPost().getPositionY() && bnext.getPositionY() >= Constants.GOAL_EAST.getLowerPost().getPositionY()) {
				reward += goal;
				//logger.info("goal");
			}

			double distBallAdvGoalprev = Constants.GOAL_EAST.getDistance(bprev) ;

			double distBallAdvGoalnext = Constants.GOAL_EAST.getDistance(bnext);

			if (distBallAdvGoalnext < distBallAdvGoalprev) {
				if (distBallAdvGoalnext < 1)  {
					reward += gettingNearGoal;
				} else {
					reward += gettingNearGoal * 1 / distBallAdvGoalnext;
				}
			} else {
				if (distBallAdvGoalnext != distBallAdvGoalprev) {
					if (distBallAdvGoalnext < 1) {
						reward += gettingAwayFromGoal;
					} else {
						reward += gettingAwayFromGoal * 1 / distBallAdvGoalnext;
					}
				}
			}
		}
		
		return reward;
	}

	public PitchSide getPitchSide() {
		return pitchSide;
	}

	public static void main(String[] args){
		List<IPlayerState> p = new ArrayList<>();
		p.add(new PlayerState(PitchSide.EAST, 1, 0, 0, 2, 1, 22.5, 0));
		logger.info(p.get(0).getVelocityX());
		logger.info(p.get(0).getVelocityY());
		p.add(new PlayerState(PitchSide.EAST, 2, 0, 0));
		p.add(new PlayerState(PitchSide.WEST, 1, 0, 0));
		p.add(new PlayerState(PitchSide.WEST, 2, 0, 0));
		State s = new State(new Ball(-20, 0), p);
		Reward r = new Reward(2000,-2000,50, -50, 70, 170, -170, PitchSide.EAST);
		List<PlayerAction> l = new ArrayList<>();
		l.add(new PlayerAction(1, new Kick(0, 0)));
		l.add(new PlayerAction(2, new Kick(0, 0)));
		PlayerActionSet a = new PlayerActionSet(l);

		p = new ArrayList<>();
		p.add(new PlayerState(PitchSide.EAST, 1, -10, 0));
		p.add(new PlayerState(PitchSide.EAST, 2, -20, 0));
		p.add(new PlayerState(PitchSide.WEST, 1, 0, 0));
		p.add(new PlayerState(PitchSide.WEST, 2, 0, 0));
		State s1 = new State(new Ball(-30, 0), p);
		logger.info(r.calculateReward(s, a, s1));
	}
}

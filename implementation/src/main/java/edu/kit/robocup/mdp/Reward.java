package edu.kit.robocup.mdp;

import java.util.List;

import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.interf.game.IPlayerState;
import edu.kit.robocup.game.state.State;

public class Reward {
	
	private int goal;
	private int advGoal;
	private int gettingNearBall;
	private int gettingAwayFromBall;
	private int havingBall;
	private int gettingNearGoal;
	private int gettingAwayFromGoal;
	
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
	public Reward(int goal, int advGoal, int gettingNearBall, int gettingAwayFromBall, int havingBall, int gettingNearGoal, int gettingAwayFromGoal) {
		this.goal = goal;
		this.advGoal = advGoal;
		this.gettingAwayFromBall = gettingAwayFromBall;
		this.gettingNearBall = gettingNearBall;
		this.havingBall = havingBall;
		this.gettingNearGoal = gettingNearGoal;
		this.gettingAwayFromGoal = gettingAwayFromGoal;
	}

	public int calculateReward(State prevState, IAction action, State nextState, String teamname, boolean isTeamEast) {
		int reward = 0;
		
		List<IPlayerState> pprev = prevState.getPlayers(teamname);
		Ball bprev = prevState.getBall();
		double bxprev = bprev.getPositionX();
		double byprev = bprev.getPositionY();
		List<IPlayerState> pnext = nextState.getPlayers(teamname);
		Ball bnext = nextState.getBall();
		double bxnext = bnext.getPositionX();
		double bynext = bnext.getPositionY();
		for (int i = 0; i < pprev.size(); i++) {
			double pxprev = pprev.get(i).getPositionX();
			double pyprev = pprev.get(i).getPositionY();
			double distBallprev = calculateDist(pxprev, pyprev, bxprev, byprev);
			double pxnext = pnext.get(i).getPositionX();
			double pynext = pnext.get(i).getPositionY();
			double distBallnext = calculateDist(pxnext, pynext, bxnext, bynext);
			if (distBallnext > distBallprev) {
				reward += gettingAwayFromBall * 1/distBallnext;
			} else {
				reward += gettingNearBall * 1/distBallnext;
			}
			// kickable margin in server.conf is 0.7
			if (distBallnext <= 0.7) {
				reward += havingBall;
			}
			
		}
		
		double pitch_length = 105.0/2.0;
		double goal_width = 14.02/2.0;
		if (isTeamEast) {
			if (bxnext == -pitch_length && Math.abs(bynext) <= goal_width) {
				reward += goal;
			}
			if (bxnext == pitch_length && Math.abs(bynext) <= goal_width) {
				reward += advGoal;
			}

			double distBallAdvGoalprev;
			if (Math.abs(bxprev) > goal_width) {
				if (bxprev > 0) {
					distBallAdvGoalprev = calculateDist(-pitch_length, goal_width, bxprev, byprev);
				} else {
					distBallAdvGoalprev = calculateDist(-pitch_length, -goal_width, bxprev, byprev);
				}
			} else {
				distBallAdvGoalprev = Math.abs(bxprev - (-pitch_length));
			}
			double distBallAdvGoalnext;
			if (Math.abs(bxnext) > goal_width) {
				if (bxnext > 0) {
					distBallAdvGoalnext = calculateDist(-pitch_length, goal_width, bxnext, bynext);
				} else {
					distBallAdvGoalnext = calculateDist(-pitch_length, -goal_width, bxnext, bynext);
				}
			} else {
				distBallAdvGoalnext = Math.abs(bxnext - (-pitch_length));
			}
			
			if (distBallAdvGoalnext < distBallAdvGoalprev) {
				reward += gettingNearGoal * 1/distBallAdvGoalnext;
			} else {
				reward += gettingAwayFromGoal * 1/distBallAdvGoalnext;
			}
		} else {
			if (bxnext == 52.5 && Math.abs(bynext) <= 7.01) {
				reward += goal;
			}
			if (bxnext == -52.5 && Math.abs(bynext) <= 7.01) {
				reward += advGoal;
			}
			double distBallAdvGoalprev;
			if (Math.abs(bxprev) > goal_width) {
				if (bxprev > 0) {
					distBallAdvGoalprev = calculateDist(pitch_length, goal_width, bxprev, byprev);
				} else {
					distBallAdvGoalprev = calculateDist(pitch_length, -goal_width, bxprev, byprev);
				}
			} else {
				distBallAdvGoalprev = Math.abs(bxprev - (pitch_length));
			}
			double distBallAdvGoalnext;
			if (Math.abs(bxnext) > goal_width) {
				if (bxnext > 0) {
					distBallAdvGoalnext = calculateDist(pitch_length, goal_width, bxnext, bynext);
				} else {
					distBallAdvGoalnext = calculateDist(pitch_length, -goal_width, bxnext, bynext);
				}
			} else {
				distBallAdvGoalnext = Math.abs(bxnext - (pitch_length));
			}
			
			if (distBallAdvGoalnext < distBallAdvGoalprev) {
				reward += gettingNearGoal * 1/distBallAdvGoalnext;
			} else {
				reward += gettingAwayFromGoal * 1/distBallAdvGoalnext;
			}
		}
		
		return reward;
	}
	
	private double calculateDist(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
	}
	
}

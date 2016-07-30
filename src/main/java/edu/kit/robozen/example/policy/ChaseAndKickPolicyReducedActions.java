package edu.kit.robozen.example.policy;

import edu.kit.robozen.constant.Constants;
import edu.kit.robozen.constant.PitchSide;
import edu.kit.robozen.game.Dash;
import edu.kit.robozen.game.Kick;
import edu.kit.robozen.game.Turn;
import edu.kit.robozen.game.controller.IPlayerController;
import edu.kit.robozen.interf.game.IAction;
import edu.kit.robozen.interf.game.IPlayerState;
import edu.kit.robozen.interf.mdp.IPolicy;
import edu.kit.robozen.interf.mdp.IState;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Policy with reduced Actions, only allowed: Turn 1, Turn 10, Turn 50, Turn -1, Turn -10, Turn -50, Dash 40, Kick (30,0), Kick (30,25), Kick(30, -25)
 */
public class ChaseAndKickPolicyReducedActions implements IPolicy {

    static Logger logger = Logger.getLogger(ChaseAndKickPolicyReducedActions.class.getName());

    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> players, PitchSide pitchSide) {
        Map<IPlayerController, IAction> action = new HashMap<>();
        for (IPlayerController playerController : players) {
            IPlayerState playerState = state.getPlayerState(playerController);
            double angle = playerState.getAngleTo(state.getBall());
            logger.info("angle " + angle);
            if (Math.abs(angle) >= 50) {
                if (angle >= 50) {
                    action.put(playerController, new Turn(50));
                } else {
                    action.put(playerController, new Turn(-50));
                }
            } else if (Math.abs(angle) >= 10) {
                if (angle >= 10) {
                    action.put(playerController, new Turn(10));
                } else {
                    action.put(playerController, new Turn(-10));
                }
            } else if (Math.abs(angle) >= 1) {
                if (angle >= 1) {
                    action.put(playerController, new Turn(1));
                } else {
                    action.put(playerController, new Turn(-1));
                }
            } else {
                if (playerState.getDistance(state.getBall()) > Constants.KICKABLE_MARGIN) {
                    action.put(playerController, new Dash(40));
                } else {
                    if (pitchSide == PitchSide.EAST) {
                        logger.info("angle to goal west " +Math.abs(playerState.getAngleTo(Constants.GOAL_WEST)));
                        if (Math.abs(playerState.getAngleTo(Constants.GOAL_WEST)) >= 50 ) {
                            if (playerState.getAngleTo(Constants.GOAL_WEST) >= 50) {
                                action.put(playerController, new Turn(50));
                            } else {
                                action.put(playerController, new Turn(-50));
                            }
                        } else if (Math.abs(playerState.getAngleTo(Constants.GOAL_WEST)) >= 20 ) {
                            if (playerState.getAngleTo(Constants.GOAL_WEST) >= 20) {
                                action.put(playerController, new Kick(30, 25));
                            } else {
                                action.put(playerController, new Kick(30, -25));
                            }
                        } else {
                            action.put(playerController, new Kick(30, 0));
                        }
                    } else {
                        logger.info("angle to goal east " + Math.abs(playerState.getAngleTo(Constants.GOAL_EAST)));
                        if (Math.abs(playerState.getAngleTo(Constants.GOAL_EAST)) >= 50 ) {
                            if (playerState.getAngleTo(Constants.GOAL_EAST) >= 50) {
                                action.put(playerController, new Turn(50));
                            } else {
                                action.put(playerController, new Turn(-50));
                            }
                        } else if (Math.abs(playerState.getAngleTo(Constants.GOAL_EAST)) >= 20 ) {
                            if (playerState.getAngleTo(Constants.GOAL_EAST) >= 20) {
                                action.put(playerController, new Kick(30, 25));
                            } else {
                                action.put(playerController, new Kick(30, -25));
                            }
                        } else {
                            action.put(playerController, new Kick(30, 0));
                        }
                    }
                }

            }
        }
        return action;
    }

}

package edu.kit.robozen.example.policy;

import edu.kit.robozen.constant.Constants;
import edu.kit.robozen.constant.PitchSide;
import edu.kit.robozen.game.*;
import edu.kit.robozen.game.controller.IPlayerController;
import edu.kit.robozen.interf.game.IAction;
import edu.kit.robozen.interf.game.IPlayerState;
import edu.kit.robozen.interf.mdp.IPolicy;
import edu.kit.robozen.interf.mdp.IState;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChaseAndKickPolicy implements IPolicy {

    static Logger logger = Logger.getLogger(ChaseAndKickPolicy.class.getName());

    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> players, PitchSide pitchSide) {
        Map<IPlayerController, IAction> action = new HashMap<>();
        for (IPlayerController playerController : players) {
            IPlayerState playerState = state.getPlayerState(playerController);
            double angle = playerState.getAngleTo(state.getBall());
            if (Math.abs(angle) >= 5) {
                action.put(playerController, new Turn((int)angle));
            } else {
                if (playerState.getDistance(state.getBall()) > Constants.KICKABLE_MARGIN) {
                    action.put(playerController, new Dash((int) Constants.maxpower)); //((int)(Math.random() * ((Constants.maxpower) + 1)))));
                } else {
                    if (pitchSide == PitchSide.EAST) {
                        action.put(playerController, new Kick((int) Constants.maxpower, (int) playerState.getAngleTo(Constants.GOAL_WEST))); //(Math.random() * ((Constants.maxpower) + 1)), (int) playerState.getAngleTo(Constants.GOAL_WEST)));
                    } else {
                        action.put(playerController, new Kick((int) Constants.maxpower, (int) playerState.getAngleTo(Constants.GOAL_EAST))); //(Math.random() * ((Constants.maxpower) + 1)), (int) playerState.getAngleTo(Constants.GOAL_EAST)));
                    }
                }

            }
        }
        return action;
    }

}

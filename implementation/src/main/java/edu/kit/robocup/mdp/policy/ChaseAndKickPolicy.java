package edu.kit.robocup.mdp.policy;

import edu.kit.robocup.game.*;
import edu.kit.robocup.game.controller.IPlayerController;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.IPlayerState;
import edu.kit.robocup.mdp.IPolicy;
import edu.kit.robocup.mdp.IState;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChaseAndKickPolicy implements IPolicy {

    static Logger logger = Logger.getLogger(ChaseAndKickPolicy.class.getName());

    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> players) {
        Map<IPlayerController, IAction> action = new HashMap<>();
        for (IPlayerController playerController : players) {
            IPlayerState playerState = state.getPlayerState(playerController);
            double angle = playerState.getAngleTo(state.getBall());
            logger.info(playerState);
            if (Math.abs(angle) >= 1) {
                action.put(playerController, new Turn((int)angle));
            } else {
                action.put(playerController, new Dash(20));
            }
        }
        return action;
    }

}

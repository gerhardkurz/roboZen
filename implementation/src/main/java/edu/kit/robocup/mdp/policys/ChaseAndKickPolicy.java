package edu.kit.robocup.mdp.policys;

import edu.kit.robocup.game.*;
import edu.kit.robocup.game.controller.IPlayerController;
import edu.kit.robocup.game.state.IPlayerState;
import edu.kit.robocup.mdp.IPolicy;
import edu.kit.robocup.mdp.IState;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by A1m on 17.05.2016.
 */
public class ChaseAndKickPolicy implements IPolicy {

    static Logger logger = Logger.getLogger(ChaseAndKickPolicy.class.getName());

    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> players) {
        Map<IPlayerController, IAction> action = new HashMap<>();
        for (IPlayerController playerController : players) {
            IPlayerState playerState = playerController.getPlayerStateFromState(state);
            logger.info("bodyangle: " + playerState.getBodyAngle());

/*
            Move move = new Move((int) state.getBall().getPositionX(), (int) state.getBall().getPositionY());
            logger.info(move);
            action.put(playerController, move);
            */
        }
        return action;
    }
}

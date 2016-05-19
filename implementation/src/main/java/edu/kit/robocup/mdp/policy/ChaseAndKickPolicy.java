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
            IPlayerState playerState = playerController.getPlayerStateFromState(state);
            double angle = getTurnAngleToFaceBall(playerState, state.getBall());


            //action.put(playerController, new Turn(45));
            action.put(playerController, new Turn((int)angle));
/*
            Move move = new Move((int) state.getBall().getPositionX(), (int) state.getBall().getPositionY());
            logger.info(move);
            action.put(playerController, move);
            */
        }
        logger.info("-------------------");
        return action;
    }

    private double getTurnAngleToFaceBall(IPlayerState playerState, Ball ball) {

        double theta = Math.atan(Math.abs(ball.getPositionY()-playerState.getPositionY())/Math.abs(ball.getPositionX()-playerState.getPositionX()));
        double thetaDeg = Math.toDegrees(theta);

        double toTurn = thetaDeg - playerState.getBodyAngle();
        double toTurnCorrected = toTurn < -180? toTurn + 360 : (toTurn > 180? toTurn - 360 : toTurn);


        logger.info("Player_" + playerState.getNumber() + " toTurnCorrected " + toTurnCorrected + " toTurn: " + toTurn + " thetaDeg: " + thetaDeg  + " BodyAngle: " + playerState.getBodyAngle());
        return toTurn;
    }
}

package edu.kit.robocup.mdp.policys;

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

/**
 * Created by A1m on 17.05.2016.
 */
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
        double relX = ball.getPositionX() - playerState.getPositionX();
        double relY = ball.getPositionY() - playerState.getPositionY();
        //double rel = relY / relX;

        double theta = Math.atan2(relX, relY);
        double thetaDeg = Math.toDegrees(theta) - 90;
        double degTurn90 = thetaDeg < -180? thetaDeg + 360 : thetaDeg;

        double toTurn = degTurn90 - playerState.getBodyAngle();
        double toTurnCorrected = toTurn < -180? toTurn + 360 : (toTurn > 180? toTurn - 360 : toTurn);


        logger.info("Player_" + playerState.getNumber() + " toTurnCorrected " + toTurnCorrected + " toTurn: " + toTurn + " thetaDeg: " + thetaDeg + " degTurn90: " + degTurn90 + " BodyAngle: " + playerState.getBodyAngle());
        return toTurn;
    }
}

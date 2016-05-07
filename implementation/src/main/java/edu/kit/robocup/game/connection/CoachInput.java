package edu.kit.robocup.game.connection;


import com.github.robocup_atan.atan.model.ActionsCoach;
import com.github.robocup_atan.atan.model.ControllerCoach;
import com.github.robocup_atan.atan.model.enums.*;
import org.apache.log4j.Logger;

import java.util.HashMap;

public class CoachInput implements ControllerCoach {
    private static Logger logger = Logger.getLogger(CoachInput.class);
    private ActionsCoach coach;

    /** {@inheritDoc} */
    @Override
    public void infoHearError(Errors error) {
        logger.error(error);
    }

    /** {@inheritDoc} */
    @Override
    public void infoHearOk(Ok ok) {
        logger.info(ok);
    }

    /** {@inheritDoc} */
    @Override
    public void infoHearPlayMode(PlayMode playMode) {
        logger.info(playMode);
    }

    /** {@inheritDoc} */
    @Override
    public void infoHearPlayer(double direction, String message) {
        logger.info(message + "from " + direction);
    }

    /** {@inheritDoc} */
    @Override
    public void infoHearReferee(RefereeMessage refereeMessage) {
        logger.info(refereeMessage);
    }

    /** {@inheritDoc} */
    @Override
    public void infoHearWarning(Warning warning) {
        logger.warn(warning);
    }

    /** {@inheritDoc} */
    @Override
    public void infoPlayerType(int id, double playerSpeedMax, double staminaIncMax, double playerDecay,
                               double inertiaMoment, double dashPowerRate, double playerSize, double kickableMargin,
                               double kickRand, double extraStamina, double effortMax, double effortMin) {
//        logger.info("player type");
    }

    /** {@inheritDoc} */
    @Override
    public void infoPlayerParam(double allowMultDefaultType, double dashPowerRateDeltaMax,
                                double dashPowerRateDeltaMin, double effortMaxDeltaFactor, double effortMinDeltaFactor,
                                double extraStaminaDeltaMax, double extraStaminaDeltaMin,
                                double inertiaMomentDeltaFactor, double kickRandDeltaFactor,
                                double kickableMarginDeltaMax, double kickableMarginDeltaMin,
                                double newDashPowerRateDeltaMax, double newDashPowerRateDeltaMin,
                                double newStaminaIncMaxDeltaFactor, double playerDecayDeltaMax,
                                double playerDecayDeltaMin, double playerTypes, double ptMax, double randomSeed,
                                double staminaIncMaxDeltaFactor, double subsMax) {
//        logger.info("player param");
    }

    /** {@inheritDoc} */
    @Override
    public void infoCPTOther(int unum) {
        logger.info("change player type other");
    }

    /** {@inheritDoc} */
    @Override
    public void infoCPTOwn(int unum, int type) {
        logger.info("change player type own");
    }

    /** {@inheritDoc} */
    @Override
    public void infoServerParam(HashMap<ServerParams, Object> info) {
//        logger.info("server param");
//        coach.teamGraphic(new XPMImageAtanLogo());
    }

    /** {@inheritDoc} */
    @Override
    public void infoSeeBall(double x, double y, double deltaX, double deltaY) {
        logger.info("see ball");
    }

    /** {@inheritDoc} */
    @Override
    public void infoSeePlayerOther(int number, boolean goalie, double x, double y, double deltaX, double deltaY,
                                   double bodyAngle, double neckAngle) {
//        logger.info("see player other");
    }

    /** {@inheritDoc} */
    @Override
    public void infoSeePlayerOwn(int number, boolean goalie, double x, double y, double deltaX, double deltaY,
                                 double bodyAngle, double neckAngle) {
//        logger.info("see player own");
    }

    /** {@inheritDoc} */
    @Override
    public void setCoach(ActionsCoach c) {
        coach = c;
    }

    /** {@inheritDoc} */
    @Override
    public ActionsCoach getCoach() {
        return coach;
    }

    /** {@inheritDoc} */
    @Override
    public void infoSeeGoalOther(double x, double y) {
        logger.info("see goal other");
    }

    /** {@inheritDoc} */
    @Override
    public void infoSeeGoalOwn(double x, double y) {
        logger.info("see goal own");
    }

    /** {@inheritDoc} */
    @Override
    public void infoHearTeamNames(String teamWest, String teamEast) {
        logger.info("Team names" + teamWest + " " + teamEast);
    }
}

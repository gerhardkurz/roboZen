package edu.kit.robocup.game.connection;

import com.github.robocup_atan.atan.model.*;
import com.github.robocup_atan.atan.model.enums.*;
import org.apache.log4j.Logger;

import java.util.HashMap;


public class PlayerInput implements ControllerPlayer {
    private final static Logger logger = Logger.getLogger(PlayerInput.class.getName());
    private final Player player;

    private ActionsPlayer actionsPlayer;

    public PlayerInput(Player player) {
        this.player = player;
    }


    @Override
    public void preInfo() {}

    @Override
    public void postInfo() {}

    @Override
    public ActionsPlayer getPlayer() {
        return actionsPlayer;
    }

    @Override
    public void setPlayer(ActionsPlayer player) {
        this.actionsPlayer = player;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public void setType(String newType) {}

    @Override
    public void infoSeeFlagRight(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {

    }

    @Override
    public void infoSeeFlagLeft(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {

    }

    @Override
    public void infoSeeFlagOwn(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {

    }

    @Override
    public void infoSeeFlagOther(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {

    }

    @Override
    public void infoSeeFlagCenter(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {

    }

    @Override
    public void infoSeeFlagCornerOwn(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
//        if (flag == Flag.LEFT)
//            logger.info(direction +  "  " + distance);
    }

    @Override
    public void infoSeeFlagCornerOther(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {

    }

    @Override
    public void infoSeeFlagPenaltyOwn(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {

    }

    @Override
    public void infoSeeFlagPenaltyOther(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {

    }

    @Override
    public void infoSeeFlagGoalOwn(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {

    }

    @Override
    public void infoSeeFlagGoalOther(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {

    }

    @Override
    public void infoSeeLine(Line line, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {

    }

    @Override
    public void infoSeePlayerOther(int number, boolean goalie, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {

    }

    @Override
    public void infoSeePlayerOwn(int number, boolean goalie, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {

    }

    @Override
    public void infoSeeBall(double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {

    }

    @Override
    public void infoHearReferee(RefereeMessage refereeMessage) {

    }

    @Override
    public void infoHearPlayMode(PlayMode playMode) {
    }

    @Override
    public void infoHearPlayer(double direction, String message) {

    }

    @Override
    public void infoHearError(Errors error) {
        logger.info(error);
    }

    @Override
    public void infoHearOk(Ok ok) {

    }

    @Override
    public void infoHearWarning(Warning warning) {

    }

    @Override
    public void infoSenseBody(ViewQuality viewQuality, ViewAngle viewAngle, double stamina, double unknown, double effort, double speedAmount, double speedDirection, double headAngle, int kickCount, int dashCount, int turnCount, int sayCount, int turnNeckCount, int catchCount, int moveCount, int changeViewCount) {

    }

    @Override
    public void infoCPTOwn(int unum, int type) {

    }

    @Override
    public void infoCPTOther(int unum) {

    }

    @Override
    public void infoPlayerType(int id, double playerSpeedMax, double staminaIncMax, double playerDecay, double inertiaMoment, double dashPowerRate, double playerSize, double kickableMargin, double kickRand, double extraStamina, double effortMax, double effortMin) {

    }

    @Override
    public void infoPlayerParam(double allowMultDefaultType, double dashPowerRateDeltaMax, double dashPowerRateDeltaMin, double effortMaxDeltaFactor, double effortMinDeltaFactor, double extraStaminaDeltaMax, double extraStaminaDeltaMin, double inertiaMomentDeltaFactor, double kickRandDeltaFactor, double kickableMarginDeltaMax, double kickableMarginDeltaMin, double newDashPowerRateDeltaMax, double newDashPowerRateDeltaMin, double newStaminaIncMaxDeltaFactor, double playerDecayDeltaMax, double playerDecayDeltaMin, double playerTypes, double ptMax, double randomSeed, double staminaIncMaxDeltaFactor, double subsMax) {

    }

    @Override
    public void infoServerParam(HashMap<ServerParams, Object> info) {

    }

    /**
     * Pause the thread.
     *
     * @param ms How long to pause the thread for (in ms).
     */
    private synchronized void pause(int ms) {
        try {
            this.wait(ms);
        } catch (InterruptedException ex) {
            logger.warn("Interrupted Exception ", ex);
        }
    }
}

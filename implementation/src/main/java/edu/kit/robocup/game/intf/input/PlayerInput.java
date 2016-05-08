package edu.kit.robocup.game.intf.input;

import com.github.robocup_atan.atan.model.*;
import com.github.robocup_atan.atan.model.enums.*;
import edu.kit.robocup.game.intf.client.Player;
import edu.kit.robocup.game.intf.parser.IPlayerInput;
import edu.kit.robocup.game.intf.parser.SeeEventType;
import edu.kit.robocup.game.intf.parser.SeeEvent;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PlayerInput implements IPlayerInput {
    private final static Logger logger = Logger.getLogger(PlayerInput.class.getName());
    private final Player player;

    private ActionsPlayer actionsPlayer;

    public PlayerInput(Player player) {
        this.player = player;
    }


    @Override
    public void preInfo() {
    }

    @Override
    public void postInfo() {
    }

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
    public void setType(String newType) {
    }

    @Override
    public void see(List<SeeEvent> seeEvents) {
        if (player.getTeamName().equals("t1")) {
            logger.info("");
            for (SeeEvent seeEvent : seeEvents) {
                logger.info(seeEvent);
            }
            logger.info("");
        }
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
        logger.warn(warning);
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


    public void see(Object o) {

    }
}

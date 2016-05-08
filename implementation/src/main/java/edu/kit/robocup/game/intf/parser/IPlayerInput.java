package edu.kit.robocup.game.intf.parser;

import com.github.robocup_atan.atan.model.ActionsPlayer;

import java.util.HashMap;


//~--- non-JDK imports --------------------------------------------------------

import com.github.robocup_atan.atan.model.enums.Errors;
import com.github.robocup_atan.atan.model.enums.Ok;
import com.github.robocup_atan.atan.model.enums.PlayMode;
import com.github.robocup_atan.atan.model.enums.RefereeMessage;
import com.github.robocup_atan.atan.model.enums.ServerParams;
import com.github.robocup_atan.atan.model.enums.ViewAngle;
import com.github.robocup_atan.atan.model.enums.ViewQuality;
import com.github.robocup_atan.atan.model.enums.Warning;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Map;

/**
 * Interface that has to be implemented in order to control players. The methods
 * are run in a cycle whenever a see command arrives from SServer. At first preInfo()
 * is invoked. then the info*() methods are called according to what kind of objects
 * are currently seen or what other commands where received from the server. At last
 * postInfo() is called. All objects are relative to the current side of the controller.
 *
 * @author Atan
 */
public interface IPlayerInput extends IInput {

    /**
     * This is the method called before the controller receives all the new visual
     * information.
     */
    public void preInfo();

    /**
     * This is the method called one all the visual information
     * has been processed.
     */
    public void postInfo();

    /**
     * Returns the current instance of the ActionsPlayer.
     *
     * @return ActionsPlayer.
     */
    public ActionsPlayer getPlayer();

    /**
     * Sets the player that the controller is controlling.
     *
     * @param c ActionsPlayer.
     */
    public void setPlayer(ActionsPlayer c);

    /**
     * Get the players type, if set.
     *
     * @return The players type (any string).
     */
    public String getType();

    /**
     * Set the players type.
     *
     * @param newType what the type will now be
     */
    public void setType(String newType);


    public void see(List<SeeEvent> seeEvents);

    /**
     * The controller is informed when a referee message is broadcast.
     *
     * @param refereeMessage possible values: FOUL_OWN, FOUL_OTHER,
     *                       HALF_TIME, TIME_UP, TIME_UP_WITHOUT_A_TEAM,
     *                       TIME_EXTENDED, DROP_BALL, OFFSIDE_OWN, OFFSIDE_OTHER
     */
    public void infoHearReferee(RefereeMessage refereeMessage);

    /**
     * The controller is informed when a play mode message is broadcast.
     *
     * @param playMode possible values: BEFORE_KICK_OFF, TIME_OVER, PLAY_ON,
     *                 KICK_OFF_OWN, KICK_OFF_OTHER, FREE_KICK_OWN,
     *                 FREE_KICK_OTHER, GOAL_KICK_OWN, CORNER_KICK_OTHER,
     *                 GOAL_KICK_OWN, GOAL_KICK_OTHER, GOAL_OWN, GOAL_OTHER
     */
    public void infoHearPlayMode(PlayMode playMode);

    /**
     * The controller is informed when it hears a message from another player.
     *
     * @param direction The direction from which the message originated.
     * @param message   The actual message said.
     */
    public void infoHearPlayer(double direction, String message);

    /**
     * The player is informed when it hears an error message.
     *
     * @param error The error to handle
     */
    public void infoHearError(Errors error);

    /**
     * The player is informed when it hears an ok message.
     *
     * @param ok The message to handle.
     */
    public void infoHearOk(Ok ok);

    /**
     * The player is informed when it hears a warning.
     *
     * @param warning The warning to handle.
     */
    public void infoHearWarning(Warning warning);

    /**
     * <p>infoSenseBody.</p>
     *
     * @param viewQuality     possible values: HIGH, LOW
     * @param viewAngle       possible values: NARROW, NORMAL, WIDE
     * @param stamina         a double.
     * @param unknown         a double.
     * @param effort          a double.
     * @param speedAmount     a double.
     * @param speedDirection  a double.
     * @param headAngle       a double.
     * @param dashCount       a int.
     * @param kickCount       a int.
     * @param turnCount       a int.
     * @param sayCount        a int.
     * @param turnNeckCount   a int.
     * @param catchCount      a int.
     * @param moveCount       a int.
     * @param changeViewCount a int.
     */
    public void infoSenseBody(ViewQuality viewQuality, ViewAngle viewAngle, double stamina, double unknown,
                              double effort, double speedAmount, double speedDirection, double headAngle,
                              int kickCount, int dashCount, int turnCount, int sayCount, int turnNeckCount,
                              int catchCount, int moveCount, int changeViewCount);

    /**
     * The controller is informed when the change player type message is received.
     *
     * @param unum The players uniform number.
     * @param type The players type.
     */
    public void infoCPTOwn(int unum, int type);

    /**
     * The controller is informed when the change player type message is received.
     *
     * @param unum The players uniform number.
     */
    public void infoCPTOther(int unum);

    /**
     * The player is informed when the player type message is received.
     *
     * @param id             a int.
     * @param playerSpeedMax a double.
     * @param staminaIncMax  a double.
     * @param playerDecay    a double.
     * @param inertiaMoment  a double.
     * @param dashPowerRate  a double.
     * @param playerSize     a double.
     * @param kickableMargin a double.
     * @param kickRand       a double.
     * @param extraStamina   a double.
     * @param effortMax      a double.
     * @param effortMin      a double.
     */
    public void infoPlayerType(int id, double playerSpeedMax, double staminaIncMax, double playerDecay,
                               double inertiaMoment, double dashPowerRate, double playerSize, double kickableMargin,
                               double kickRand, double extraStamina, double effortMax, double effortMin);

    /**
     * The player is informed when the player param message is received.
     *
     * @param allowMultDefaultType        a double.
     * @param dashPowerRateDeltaMax       a double.
     * @param dashPowerRateDeltaMin       a double.
     * @param effortMaxDeltaFactor        a double.
     * @param effortMinDeltaFactor        a double.
     * @param extraStaminaDeltaMax        a double.
     * @param extraStaminaDeltaMin        a double.
     * @param inertiaMomentDeltaFactor    a double.
     * @param kickRandDeltaFactor         a double.
     * @param kickableMarginDeltaMax      a double.
     * @param kickableMarginDeltaMin      a double.
     * @param newDashPowerRateDeltaMax    a double.
     * @param newDashPowerRateDeltaMin    a double.
     * @param newStaminaIncMaxDeltaFactor a double.
     * @param playerDecayDeltaMax         a double.
     * @param playerDecayDeltaMin         a double.
     * @param playerTypes                 a double.
     * @param ptMax                       a double.
     * @param randomSeed                  a double.
     * @param staminaIncMaxDeltaFactor    a double.
     * @param subsMax                     a double.
     */
    public void infoPlayerParam(double allowMultDefaultType, double dashPowerRateDeltaMax,
                                double dashPowerRateDeltaMin, double effortMaxDeltaFactor, double effortMinDeltaFactor,
                                double extraStaminaDeltaMax, double extraStaminaDeltaMin,
                                double inertiaMomentDeltaFactor, double kickRandDeltaFactor,
                                double kickableMarginDeltaMax, double kickableMarginDeltaMin,
                                double newDashPowerRateDeltaMax, double newDashPowerRateDeltaMin,
                                double newStaminaIncMaxDeltaFactor, double playerDecayDeltaMax,
                                double playerDecayDeltaMin, double playerTypes, double ptMax, double randomSeed,
                                double staminaIncMaxDeltaFactor, double subsMax);

    /**
     * The controller is informed when the server param message is received.
     *
     * @param info A hashmap containing all the server param details.
     */
    public void infoServerParam(HashMap<ServerParams, Object> info);
}


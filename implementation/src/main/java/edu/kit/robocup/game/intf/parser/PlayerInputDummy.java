package edu.kit.robocup.game.intf.parser;

import com.github.robocup_atan.atan.model.ActionsPlayer;
import com.github.robocup_atan.atan.model.ControllerPlayer;
import com.github.robocup_atan.atan.model.enums.*;
import edu.kit.robocup.game.intf.input.PlayerInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerInputDummy implements IInputDummy {
    private final PlayerInput input;
    private final List<SeeEvent> seeEvents = new ArrayList<>();

    public PlayerInputDummy(PlayerInput input) {
        this.input = input;
    }

    @Override
    public void updateInput() {
        if (!seeEvents.isEmpty()) {
            input.see(seeEvents);
            seeEvents.clear();
        }
    }

    @Override
    public void seeAmbiguous(SeeEventType eventType, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        seeEvents.add(new SeeEvent(eventType, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection));
    }


    @Override
    public void infoSeeFlagRight(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        seeEvents.add(new SeeEvent(SeeEventType.FLAG_RIGHT, flag, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection));
    }

    @Override
    public void infoSeeFlagLeft(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        seeEvents.add(new SeeEvent(SeeEventType.FLAG_LEFT, flag, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection));
    }

    @Override
    public void infoSeeFlagOwn(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        seeEvents.add(new SeeEvent(SeeEventType.FLAG_OWN, flag, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection));
    }

    @Override
    public void infoSeeFlagOther(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        seeEvents.add(new SeeEvent(SeeEventType.FLAG_OTHER, flag, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection));
    }

    @Override
    public void infoSeeFlagCenter(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        seeEvents.add(new SeeEvent(SeeEventType.FLAG_CENTER, flag, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection));
    }

    @Override
    public void infoSeeFlagCornerOwn(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        seeEvents.add(new SeeEvent(SeeEventType.FLAG_CORNER_OWN, flag, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection));
    }

    @Override
    public void infoSeeFlagCornerOther(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        seeEvents.add(new SeeEvent(SeeEventType.FLAG_CORNER_OTHER, flag, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection));
    }

    @Override
    public void infoSeeFlagPenaltyOwn(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        seeEvents.add(new SeeEvent(SeeEventType.FLAG_PENALTY_OWN, flag, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection));
    }

    @Override
    public void infoSeeFlagPenaltyOther(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        seeEvents.add(new SeeEvent(SeeEventType.FLAG_PENALTY_OTHER, flag, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection));
    }

    @Override
    public void infoSeeFlagGoalOwn(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        seeEvents.add(new SeeEvent(SeeEventType.FLAG_GOAL_OWN, flag, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection));
    }

    @Override
    public void infoSeeFlagGoalOther(Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        seeEvents.add(new SeeEvent(SeeEventType.FLAG_GOAL_OTHER, flag, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection));
    }

    @Override
    public void infoSeeLine(Line line, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        seeEvents.add(new SeeEvent(SeeEventType.LINE, line, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection));
    }

    @Override
    public void infoSeePlayerOther(int number, boolean goalie, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        seeEvents.add(new SeeEvent(SeeEventType.PLAYER_OTHER, number, goalie, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection));
    }

    @Override
    public void infoSeePlayerOwn(int number, boolean goalie, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        seeEvents.add(new SeeEvent(SeeEventType.PLAYER_OWN, number, goalie, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection));
    }

    @Override
    public void infoSeeBall(double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        seeEvents.add(new SeeEvent(SeeEventType.BALL, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection));
    }

    @Override
    public void preInfo() {
        input.preInfo();
    }

    @Override
    public void postInfo() {
        input.postInfo();
    }

    @Override
    public ActionsPlayer getPlayer() {
        return input.getPlayer();
    }

    @Override
    public void setPlayer(ActionsPlayer c) {
        input.setPlayer(c);
    }

    @Override
    public String getType() {
        return input.getType();
    }

    @Override
    public void setType(String newType) {
        input.setType(newType);
    }

    @Override
    public void infoHearReferee(RefereeMessage refereeMessage) {
        input.infoHearReferee(refereeMessage);
    }

    @Override
    public void infoHearPlayMode(PlayMode playMode) {
        input.infoHearPlayMode(playMode);
    }

    @Override
    public void infoHearPlayer(double direction, String message) {
        input.infoHearPlayer(direction, message);
    }

    @Override
    public void infoHearError(Errors error) {
        input.infoHearError(error);
    }

    @Override
    public void infoHearOk(Ok ok) {
        input.infoHearOk(ok);
    }

    @Override
    public void infoHearWarning(Warning warning) {
        input.infoHearWarning(warning);
    }

    @Override
    public void infoSenseBody(ViewQuality viewQuality, ViewAngle viewAngle, double stamina, double unknown, double effort, double speedAmount, double speedDirection, double headAngle, int kickCount, int dashCount, int turnCount, int sayCount, int turnNeckCount, int catchCount, int moveCount, int changeViewCount) {
        input.infoSenseBody(viewQuality, viewAngle, stamina, unknown, effort, speedAmount, speedDirection, headAngle, kickCount, dashCount, turnCount, sayCount, turnNeckCount, catchCount, moveCount, changeViewCount);
    }

    @Override
    public void infoCPTOwn(int unum, int type) {
        input.infoCPTOwn(unum, type);
    }

    @Override
    public void infoCPTOther(int unum) {
        input.infoCPTOther(unum);
    }

    @Override
    public void infoPlayerType(int id, double playerSpeedMax, double staminaIncMax, double playerDecay, double inertiaMoment, double dashPowerRate, double playerSize, double kickableMargin, double kickRand, double extraStamina, double effortMax, double effortMin) {
        input.infoPlayerType(id, playerSpeedMax, staminaIncMax, playerDecay, inertiaMoment, dashPowerRate, playerSize, kickableMargin, kickRand, extraStamina, effortMax, effortMin);
    }

    @Override
    public void infoPlayerParam(double allowMultDefaultType, double dashPowerRateDeltaMax, double dashPowerRateDeltaMin, double effortMaxDeltaFactor, double effortMinDeltaFactor, double extraStaminaDeltaMax, double extraStaminaDeltaMin, double inertiaMomentDeltaFactor, double kickRandDeltaFactor, double kickableMarginDeltaMax, double kickableMarginDeltaMin, double newDashPowerRateDeltaMax, double newDashPowerRateDeltaMin, double newStaminaIncMaxDeltaFactor, double playerDecayDeltaMax, double playerDecayDeltaMin, double playerTypes, double ptMax, double randomSeed, double staminaIncMaxDeltaFactor, double subsMax) {
        input.infoPlayerParam(allowMultDefaultType, dashPowerRateDeltaMax, dashPowerRateDeltaMin, effortMaxDeltaFactor, effortMinDeltaFactor, extraStaminaDeltaMax, extraStaminaDeltaMin, inertiaMomentDeltaFactor, kickRandDeltaFactor, kickableMarginDeltaMax, kickableMarginDeltaMin, newDashPowerRateDeltaMax, newDashPowerRateDeltaMin, newStaminaIncMaxDeltaFactor, playerDecayDeltaMax, playerDecayDeltaMin, playerTypes, ptMax, randomSeed, staminaIncMaxDeltaFactor, subsMax);
    }

    @Override
    public void infoServerParam(HashMap<ServerParams, Object> info) {
        input.infoServerParam(info);
    }


}

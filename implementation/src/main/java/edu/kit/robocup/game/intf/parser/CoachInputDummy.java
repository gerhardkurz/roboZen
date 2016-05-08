package edu.kit.robocup.game.intf.parser;

import com.github.robocup_atan.atan.model.ActionsCoach;
import com.github.robocup_atan.atan.model.ControllerCoach;
import com.github.robocup_atan.atan.model.enums.*;

import java.util.HashMap;


public class CoachInputDummy implements IInputDummy, ControllerCoach {

    @Override
    public void infoSeePlayerOwn(int number, boolean goalie, double x, double y, double deltaX, double deltaY, double bodyAngle, double neckAngle) {

    }

    @Override
    public void infoSeePlayerOther(int number, boolean goalie, double x, double y, double deltaX, double deltaY, double bodyAngle, double neckAngle) {

    }

    @Override
    public void infoSeeGoalOwn(double x, double y) {

    }

    @Override
    public void infoSeeGoalOther(double x, double y) {

    }

    @Override
    public void infoSeeBall(double x, double y, double deltaX, double deltaY) {

    }

    @Override
    public void infoHearTeamNames(String teamWest, String teamEast) {

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

    }

    @Override
    public void infoHearOk(Ok ok) {

    }

    @Override
    public void infoHearWarning(Warning warning) {

    }

    @Override
    public void infoPlayerType(int id, double playerSpeedMax, double staminaIncMax, double playerDecay, double inertiaMoment, double dashPowerRate, double playerSize, double kickableMargin, double kickRand, double extraStamina, double effortMax, double effortMin) {

    }

    @Override
    public void infoCPTOwn(int unum, int type) {

    }

    @Override
    public void infoCPTOther(int unum) {

    }

    @Override
    public void infoPlayerParam(double allowMultDefaultType, double dashPowerRateDeltaMax, double dashPowerRateDeltaMin, double effortMaxDeltaFactor, double effortMinDeltaFactor, double extraStaminaDeltaMax, double extraStaminaDeltaMin, double inertiaMomentDeltaFactor, double kickRandDeltaFactor, double kickableMarginDeltaMax, double kickableMarginDeltaMin, double newDashPowerRateDeltaMax, double newDashPowerRateDeltaMin, double newStaminaIncMaxDeltaFactor, double playerDecayDeltaMax, double playerDecayDeltaMin, double playerTypes, double ptMax, double randomSeed, double staminaIncMaxDeltaFactor, double subsMax) {

    }

    @Override
    public void infoServerParam(HashMap<ServerParams, Object> info) {

    }

    @Override
    public void setCoach(ActionsCoach c) {

    }

    @Override
    public ActionsCoach getCoach() {
        return null;
    }

    @Override
    public void updateInput() {

    }

    @Override
    public void seeAmbiguous(SeeEventType eventType, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {

    }
}

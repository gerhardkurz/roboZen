package edu.kit.robocup.game.intf.parser;

import com.github.robocup_atan.atan.model.ControllerTrainer;
import com.github.robocup_atan.atan.model.enums.*;



public class TrainerInputDummy implements IInputDummy, ControllerTrainer {
    @Override
    public void infoHearPlayer(double direction, String message) {

    }

    @Override
    public void infoHearPlayMode(PlayMode playMode) {

    }

    @Override
    public void infoHearReferee(RefereeMessage refereeMessage) {

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
    public void updateInput() {

    }

    @Override
    public void seeAmbiguous(SeeEventType eventType, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {

    }
}

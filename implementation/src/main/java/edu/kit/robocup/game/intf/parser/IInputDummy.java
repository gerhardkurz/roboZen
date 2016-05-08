package edu.kit.robocup.game.intf.parser;


import com.github.robocup_atan.atan.model.ControllerPlayer;

public interface IInputDummy {
    void updateInput();
    void seeAmbiguous(SeeEventType eventType, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection);
}

package edu.kit.robocup.game.intf.parser;

//~--- non-JDK imports --------------------------------------------------------

import com.github.robocup_atan.atan.model.ControllerCoach;
import com.github.robocup_atan.atan.model.ControllerPlayer;
import com.github.robocup_atan.atan.model.ControllerTrainer;
import com.github.robocup_atan.atan.parser.objects.ObjName;

/**
 * A null parser object. Used if the parser cannot determine the object to stop runtime errors.
 *
 * @author Atan
 */
public class ObjNameNull implements ObjName {
    private final SeeEventType eventType;


    public ObjNameNull(SeeEventType eventType) {
        this.eventType = eventType;
    }

    private void seeAmbiguous(ControllerPlayer c, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        IInputDummy inputDummy = (IInputDummy) c;
        inputDummy.seeAmbiguous(eventType, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void infoSeeFromEast(ControllerPlayer c, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        seeAmbiguous(c, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void infoSeeFromWest(ControllerPlayer c, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        seeAmbiguous(c, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void infoSeeFromEast(ControllerCoach c, double x, double y, double deltaX, double deltaY, double bodyAngle, double neckAngle) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void infoSeeFromWest(ControllerCoach c, double x, double y, double deltaX, double deltaY, double bodyAngle, double neckAngle) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void infoSee(ControllerTrainer c) {
    }
}

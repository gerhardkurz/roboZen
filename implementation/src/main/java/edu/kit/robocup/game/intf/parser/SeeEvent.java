package edu.kit.robocup.game.intf.parser;


import com.github.robocup_atan.atan.model.enums.Flag;
import com.github.robocup_atan.atan.model.enums.Line;

public class SeeEvent {
    private final SeeEventType eventType;
    private final Flag flag;
    private final Line line;
    private final Integer number;
    private final Boolean goalie;
    private final Double distance;
    private final Double direction;
    private final Double distChange;
    private final Double dirChange;
    private final Double bodyFacingDirection;
    private final Double headFacingDirection;

    public SeeEvent(SeeEventType eventType, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        this(eventType, null, null, null, null, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection);
    }

    public SeeEvent(SeeEventType eventType, Flag flag, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        this(eventType, flag, null, null, null, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection);
    }

    public SeeEvent(SeeEventType eventType, Line line, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        this(eventType, null, line, null, null, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection);
    }

    public SeeEvent(SeeEventType eventType, Integer number, boolean goalie, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        this(eventType, null, null, number, goalie, distance, direction, distChange, dirChange, bodyFacingDirection, headFacingDirection);
    }

    public SeeEvent(SeeEventType eventType, Flag flag, Line line, Integer number, Boolean goalie, double distance, double direction, double distChange, double dirChange, double bodyFacingDirection, double headFacingDirection) {
        this.eventType = eventType;
        this.flag = flag;
        this.line = line;
        this.number = number;
        this.goalie = goalie;
        this.distance = distance;
        this.direction = direction;
        this.distChange = distChange;
        this.dirChange = dirChange;
        this.bodyFacingDirection = bodyFacingDirection;
        this.headFacingDirection = headFacingDirection;
    }

    public Integer getNumber() {
        return number;
    }

    public Boolean getGoalie() {
        return goalie;
    }

    public double getDistance() {
        return distance;
    }

    public double getDirection() {
        return direction;
    }

    public double getDistChange() {
        return distChange;
    }

    public double getDirChange() {
        return dirChange;
    }

    public double getBodyFacingDirection() {
        return bodyFacingDirection;
    }

    public double getHeadFacingDirection() {
        return headFacingDirection;
    }
}

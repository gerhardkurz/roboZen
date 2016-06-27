package edu.kit.robocup.interf.game;


import edu.kit.robocup.constant.Constants;

import java.io.Serializable;

public interface IGameObject extends Serializable {
    double getPositionX();
    double getPositionY();

    default double getDistance(IGameObject other) {
        return Math.hypot(getPositionX() - other.getPositionX(), getPositionY() - other.getPositionY());
    }

    default double getDistanceToGoal(boolean east) {
        if (east)
            return Constants.GOAL_EAST.getDistance(this);
        else
            return Constants.GOAL_WEST.getDistance(this);
    }
}

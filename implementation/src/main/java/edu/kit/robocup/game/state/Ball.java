package edu.kit.robocup.game.state;

import edu.kit.robocup.interf.game.IMoveAbleObject;
import edu.kit.robocup.game.SimpleGameObject;

import java.io.Serializable;


public class Ball extends SimpleGameObject implements IMoveAbleObject, Serializable {
    private final double velocityX;
    private final double velocityY;

    public Ball(double positionX, double positionY) {
        this(positionX, positionY, 0, 0);
    }

    public Ball(double positionX, double positionY, double velocityX, double velocityY) {
        super(positionX, positionY);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    @Override
    public String toString() {
        return "Ball{x:" + getPositionX() + " y: " + getPositionY() + " velocity: " + velocityX + ", " + velocityY + "}";
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

}


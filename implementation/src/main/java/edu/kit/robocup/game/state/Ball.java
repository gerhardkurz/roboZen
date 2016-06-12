package edu.kit.robocup.game.state;

import edu.kit.robocup.interf.game.IMoveAbleObject;
import edu.kit.robocup.game.SimpleGameObject;

import java.io.Serializable;


public class Ball extends SimpleGameObject implements IMoveAbleObject, Serializable {
    private final double velocityLength;


    public Ball(double positionX, double positionY) {
        this(positionX, positionY, 0);
    }

    public Ball(double positionX, double positionY, double velocityLength) {
        super(positionX, positionY);
        this.velocityLength = velocityLength;
    }

    public Ball(double positionX, double positionY, double velocityX, double velocityY) {
        super(positionX, positionY);
        this.velocityLength = Math.sqrt(velocityX*velocityX+velocityY*velocityY);
    }

    @Override
    public String toString() {
        return "Ball{x:" + getPositionX() + " y: " + getPositionY() + " velocitylength: " + velocityLength + "}";
    }

    @Override
    public double getVelocityLength() {
        return velocityLength;
    }

}


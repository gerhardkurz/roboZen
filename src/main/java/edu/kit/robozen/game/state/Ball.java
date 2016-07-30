package edu.kit.robozen.game.state;

import edu.kit.robozen.interf.game.IMoveAbleObject;
import edu.kit.robozen.game.SimpleGameObject;

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

    @Override
    public double getVelocityX() {
        return velocityX;
    }

    @Override
    public double getVelocityY() {
        return velocityY;
    }

}


package edu.kit.robocup.game.state;

import edu.kit.robocup.game.IMoveAbleObject;

import java.io.Serializable;

public class Ball implements IMoveAbleObject, Serializable {
    private final double positionX;
    private final double positionY;
    private final double velocityX;
    private final double velocityY;


    public Ball(double positionX, double positionY) {
        this(positionX, positionY, 0, 0);
    }

    public Ball(double positionX, double positionY, double velocityX, double velocityY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    @Override
    public String toString() {
        return "Ball{x:" + getPositionX() + " y: " + getPositionY() + " velX: " + getVelocityX() + " velY: " + getVelocityY() + "}";
    }

    @Override
    public double getPositionX() {
        return positionX;
    }

    @Override
    public double getPositionY() {
        return positionY;
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


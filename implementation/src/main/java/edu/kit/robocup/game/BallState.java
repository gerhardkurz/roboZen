package edu.kit.robocup.game;

public class BallState extends AGameObject {
    public BallState(double positionX, double positionY, double velocityX, double velocityY) {
        super(positionX, positionY, velocityX, velocityY);
    }

    @Override
    public String toString() {
        return "BallState{x:" + getPositionX() + " y: " + getPositionY() + " velX: " + getVelocityX() + " velY: " + getVelocityY() + "}";
    }
}


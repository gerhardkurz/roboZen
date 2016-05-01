package edu.kit.robocup.game;

public class Ball extends AGameObject {
    private double velocityX;
    private double velocityY;

    public Ball(double positionX, double positionY) {
        super(positionX, positionY);
        this.velocityX = velocityY;
        this.velocityY = velocityY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }
}


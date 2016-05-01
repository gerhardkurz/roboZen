package edu.kit.robocup.game;

public abstract class AGameObject {
    private double positionX;
    private double positionY;

    public AGameObject(double positionX, double positionY) {
        updatePosition(positionX, positionY);
    }

    public void updatePosition(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }
}

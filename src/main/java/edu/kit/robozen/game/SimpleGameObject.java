package edu.kit.robozen.game;


import edu.kit.robozen.interf.game.IGameObject;

public class SimpleGameObject implements IGameObject {
    private final double positionX;
    private final double positionY;

    public SimpleGameObject(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    @Override
    public double getPositionX() {
        return positionX;
    }

    @Override
    public double getPositionY() {
        return positionY;
    }

    public String toString() {
        return "x: " + positionX + "\ny: " + positionY;
    }
}

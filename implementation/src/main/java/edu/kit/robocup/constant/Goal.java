package edu.kit.robocup.constant;


import edu.kit.robocup.game.IGameObject;

public class Goal implements IGameObject {
    private final double positionX;
    private final double positionY;

    public Goal(double positionX, double positionY) {
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
}

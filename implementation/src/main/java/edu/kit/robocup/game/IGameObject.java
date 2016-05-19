package edu.kit.robocup.game;

public interface IGameObject {
    double getPositionX();
    double getPositionY();
    double getVelocityX();
    double getVelocityY();

    default double getDistance(IGameObject other) {
        return Math.hypot(getPositionX() - other.getPositionX(), getPositionY() - other.getPositionY());
    }
}

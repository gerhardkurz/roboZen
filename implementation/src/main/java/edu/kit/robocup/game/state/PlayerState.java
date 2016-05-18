package edu.kit.robocup.game.state;


import java.io.Serializable;

public class PlayerState implements IPlayerState, Serializable {
    private final String teamName;
    private final int number;
    private final double positionX;
    private final double positionY;
    private final double velocityX;
    private final double velocityY;
    private final double neckAngle;
    private final double bodyAngle;

    public PlayerState(String teamName, int number, double positionX, double positionY) {
        this(teamName, number, positionX, positionY, 0, 0, 0, 0);
    }

    public PlayerState(String teamName, int number, double positionX, double positionY, double velocityX, double velocityY, double bodyAngle, double neckAngle) {
        this.teamName = teamName;
        this.number = number;
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.bodyAngle = bodyAngle;
        this.neckAngle = neckAngle;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getNumber() {
        return number;
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

    @Override
    public double getNeckAngle() {
        return neckAngle;
    }

    @Override
    public double getBodyAngle() {
        return bodyAngle;
    }

    public String toString() {
        return "Team " + teamName + " Number: " + number + " Position: (" + positionX + ", " + positionY + ") Velocity: (" + velocityX + ", " + velocityY + ") Bodyangle: " + bodyAngle;
    }
}

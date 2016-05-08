package edu.kit.robocup.game;


public class PlayerState extends AGameObject implements IPlayer {
    private final String teamName;
    private final int number;
    private double orientation;

    public PlayerState(String teamName, int number, double positionX, double positionY, double velocityX, double velocityY, double orientation) {
        super(positionX, positionY, velocityX, velocityY);
        this.teamName = teamName;
        this.number = number;
        this.orientation = orientation;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getNumber() {
        return number;
    }

    public double getOrientation() {
        return orientation;
    }

    public void setOrientation(double orientation) {
        this.orientation = orientation;
    }
}

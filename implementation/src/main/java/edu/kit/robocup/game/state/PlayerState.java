package edu.kit.robocup.game.state;


import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.interf.game.IPlayer;
import edu.kit.robocup.interf.game.IPlayerState;

import java.io.Serializable;


public class PlayerState implements IPlayerState, Serializable {
    private final PitchSide pitchSide;
    private final int number;
    private final double positionX;
    private final double positionY;
    private final double velocityLength;
    private final double neckAngle;
    private final double bodyAngle;

    public PlayerState(PitchSide pitchSide, int number, double positionX, double positionY) {
        this(pitchSide, number, positionX, positionY, 0, 0, 0);
    }

    public PlayerState(PitchSide pitchSide, int number, double positionX, double positionY, double velocityLength, double bodyAngle, double neckAngle) {
        this.pitchSide = pitchSide;
        this.number = number;
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityLength = velocityLength;
        this.bodyAngle = bodyAngle;
        this.neckAngle = neckAngle;
    }
    public PlayerState(PitchSide pitchSide, int number, double positionX, double positionY, double velocityX, double velocityY, double bodyAngle, double neckAngle) {
        this.pitchSide = pitchSide;
        this.number = number;
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityLength = Math.sqrt(velocityX*velocityX+velocityY*velocityY);
        this.bodyAngle = bodyAngle;
        this.neckAngle = neckAngle;
    }

    @Override
    public PitchSide getPitchSide() {
        return pitchSide;
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

    public double getVelocityLength() {
        return velocityLength;
    }

    @Override
    public double getVelocityX() {
        return Math.sin(Math.toRadians(bodyAngle)) * velocityLength;
    }

    @Override
    public double getVelocityY() {
        return Math.cos(Math.toRadians(bodyAngle)) * velocityLength;
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
        return "PitchSide " + pitchSide.toString() + " Number: " + number + " Position: (" + positionX + ", " + positionY + ") Velocitylength: " + velocityLength + " Bodyangle: " + bodyAngle;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !IPlayer.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        IPlayer player = (IPlayer) obj;
        return pitchSide == player.getPitchSide() && getNumber() == player.getNumber();
    }
}

package edu.kit.robocup.game.state;


import edu.kit.robocup.game.IGameObject;
import edu.kit.robocup.game.IMoveAbleObject;
import edu.kit.robocup.game.IPlayer;

public interface IPlayerState extends IPlayer, IMoveAbleObject {

    double getNeckAngle();
    double getBodyAngle();

    default double getAngleTo(IGameObject gameObject) {
        double x1 = Math.toDegrees(Math.cos(Math.toRadians(getBodyAngle())));
        double y1 = Math.toDegrees(Math.sin(Math.toRadians(getBodyAngle())));
        double x2 = gameObject.getPositionX() - getPositionX();
        double y2 = gameObject.getPositionY() - getPositionY();

        return Math.toDegrees(Math.atan2(x1 * y2 - y1 * x2, x1 * x2 + y1 * y2));
    }
}

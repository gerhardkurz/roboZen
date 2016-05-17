package edu.kit.robocup.game.state;


import edu.kit.robocup.game.IGameObject;
import edu.kit.robocup.game.IPlayer;

public interface IPlayerState extends IPlayer, IGameObject {

    double getNeckAngle();
    double getBodyAngle();
}

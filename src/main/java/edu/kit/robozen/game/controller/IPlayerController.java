package edu.kit.robozen.game.controller;


import edu.kit.robozen.game.*;
import edu.kit.robozen.interf.game.IAction;
import edu.kit.robozen.interf.game.IPlayer;

import java.io.Serializable;

public interface IPlayerController extends IPlayer, Serializable {
    void dash(Dash dashAction);
    void kick(Kick kickAction);
    void move(Move moveAction);
    void say(String message);
    void turn(Turn turnAction);
    void turnNeck(double angle);
    void catchBall(double direction);
    void execute(IAction action);
    Team getTeam();
}

package edu.kit.robocup.game.controller;


import edu.kit.robocup.game.IPlayer;

public interface IPlayerController extends IPlayer {
    void dash(int power);
    void kick(int power, double direction);
    void move(int x, int y);
    void say(String message);
    void turn(double angle);
    void turnNeck(double angle);
    void catchBall(double direction);
}

package edu.kit.robozen.interf.game;


import edu.kit.robozen.constant.PitchSide;

public interface IPlayer {
    PitchSide getPitchSide();
    int getNumber();
    double getVelocityLength();
}

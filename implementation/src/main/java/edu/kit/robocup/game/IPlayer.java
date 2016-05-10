package edu.kit.robocup.game;


public interface IPlayer {
    String getTeamName();
    int getNumber();

    default boolean equals(IPlayer player) {
        return getTeamName().equals(player.getTeamName()) && getNumber() == player.getNumber();
    }
}

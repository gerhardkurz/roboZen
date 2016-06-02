package edu.kit.robocup.game.server.client;


import edu.kit.robocup.game.controller.Team;

public abstract class StaffClientBase extends UDPClientBase {
    protected final Team team;

    public StaffClientBase(Team team, int port, String hostname) {
        super(port, hostname);
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }
}

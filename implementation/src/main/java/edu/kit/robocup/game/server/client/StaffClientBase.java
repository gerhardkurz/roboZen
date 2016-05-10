package edu.kit.robocup.game.server.client;


import edu.kit.robocup.game.Team;
import edu.kit.robocup.game.server.client.UDPClientBase;

public abstract class StaffClientBase extends UDPClientBase {
    protected final Team team;

    public StaffClientBase(Team team, int port, String hostname) {
        super(port, hostname);
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public String getTeamName() {
        return team.getTeamName();
    }

    public boolean isTeam(String teamName) {
        return teamName.equals(getTeamName());
    }
}

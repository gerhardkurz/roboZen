package edu.kit.robocup.game.intf.client;


public abstract class StaffBase extends UDPClientBase {
    protected boolean isTeamEast;
    protected final Team team;

    public StaffBase(Team team, int port, String hostname) {
        super(port, hostname);
        this.team = team;
    }


    public boolean isTeamEast() {
        return isTeamEast;
    }

    public String getTeamName() {
        return team.getTeamName();
    }

    public void setTeamEast(boolean teamEast) {
        isTeamEast = teamEast;
    }
}

package edu.kit.robocup.game.intf.client;


public abstract class StaffBase extends UDPClientBase {
    protected boolean isTeamEast;
    protected String teamName;

    public StaffBase(String teamName, int port, String hostname) {
        super(port, hostname);
        this.teamName = teamName;
    }


    public boolean isTeamEast() {
        return isTeamEast;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamEast(boolean teamEast) {
        isTeamEast = teamEast;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

}

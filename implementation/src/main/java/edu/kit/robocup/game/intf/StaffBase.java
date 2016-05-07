package edu.kit.robocup.game.intf;


public class StaffBase {

    private boolean isTeamEast;
    private String teamName;

    public StaffBase(String teamName) {
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

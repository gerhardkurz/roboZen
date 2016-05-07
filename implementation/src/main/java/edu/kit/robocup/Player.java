package edu.kit.robocup;



public class Player {
    private final PlayerInput input;
    private final PlayerOutput output;

    private boolean isTeamEast;
    private String teamName;
    private int number;

    public Player(String teamName, int number) {
        this.teamName = teamName;
        this.number = number;
        input = new PlayerInput(this);
        output = new PlayerOutput(this, teamName);
        input.setPlayer(output);
    }

    public PlayerInput getInput() {
        return input;
    }

    public PlayerOutput getOutput() {
        return output;
    }

    public boolean isTeamEast() {
        return isTeamEast;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getNumber() {
        return number;
    }

    public void setTeamEast(boolean teamEast) {
        isTeamEast = teamEast;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

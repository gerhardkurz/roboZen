package edu.kit.robocup.game.connection;



public class Coach {
    private final CoachInput input;
    private final CoachOutput output;

    public Coach(String teamName) {
        input = new CoachInput();
        output = new CoachOutput(teamName, input);
        input.setCoach(output);
    }

    public CoachInput getInput() {
        return input;
    }

    public CoachOutput getOutput() {
        return output;
    }
}

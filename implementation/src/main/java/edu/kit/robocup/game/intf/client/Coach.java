package edu.kit.robocup.game.intf.client;


import edu.kit.robocup.game.intf.input.CoachInput;
import edu.kit.robocup.game.intf.output.CoachOutput;

public class Coach extends StaffBase {
    private final CoachInput input;
    private final CoachOutput output;

    public Coach(String teamName) {
        super(teamName);
        input = new CoachInput();
        output = new CoachOutput(this, teamName);
        input.setCoach(output);
    }


    public CoachInput getInput() {
        return input;
    }

    public CoachOutput getOutput() {
        return output;
    }
}

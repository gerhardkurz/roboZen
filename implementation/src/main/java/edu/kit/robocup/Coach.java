package edu.kit.robocup;

import com.github.robocup_atan.atan.model.ActionsCoach;
import com.github.robocup_atan.atan.model.SServerCoach;


public class Coach {
    private final CoachInput input;
    private final SServerCoach output;

    public Coach(String teamName) {
        input = new CoachInput();
        output = new SServerCoach(teamName, input);
        input.setCoach(output);
    }

    public CoachInput getInput() {
        return input;
    }

    public SServerCoach getOutput() {
        return output;
    }
}

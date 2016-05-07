package edu.kit.robocup.game.intf.client;


import edu.kit.robocup.game.intf.input.TrainerInput;
import edu.kit.robocup.game.intf.output.TrainerOutput;

public class Trainer {
    private final TrainerInput input;
    private final TrainerOutput output;

    public Trainer(String teamName) {
        input = new TrainerInput();
        output = new TrainerOutput(this, teamName);
    }

    public TrainerInput getInput() {
        return input;
    }

    public TrainerOutput getOutput() {
        return output;
    }
}

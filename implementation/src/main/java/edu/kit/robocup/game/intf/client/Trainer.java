package edu.kit.robocup.game.intf.client;


import edu.kit.robocup.game.intf.input.TrainerInput;
import edu.kit.robocup.game.intf.output.TrainerOutput;
import edu.kit.robocup.game.intf.parser.ITrainerInput;

public class Trainer {
    private final ITrainerInput input;
    private final TrainerOutput output;

    public Trainer(String teamName) {
        input = new TrainerInput();
        output = new TrainerOutput(this, teamName);
    }

    public ITrainerInput getInput() {
        return input;
    }

    public TrainerOutput getOutput() {
        return output;
    }
}

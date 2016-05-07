package edu.kit.robocup.game.intf;


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

package edu.kit.robocup.game.connection;


public class Trainer {
    private final TrainerInput input;
    private final TrainerOutput output;

    public Trainer(String teamName) {
        input = new TrainerInput();
        output = new TrainerOutput(teamName, input);
    }

    public TrainerInput getInput() {
        return input;
    }

    public TrainerOutput getOutput() {
        return output;
    }
}

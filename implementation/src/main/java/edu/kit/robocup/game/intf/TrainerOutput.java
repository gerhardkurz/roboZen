package edu.kit.robocup.game.intf;

//~--- non-JDK imports --------------------------------------------------------

import com.github.robocup_atan.atan.model.*;
import com.github.robocup_atan.atan.model.enums.PlayMode;

import com.github.robocup_atan.atan.parser.trainer.CmdParserTrainer;

import org.apache.log4j.Logger;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.StringReader;

/**
 * A simple implementation of AbstractUDPClient for Trainers.
 *
 * @author Atan
 */
public class TrainerOutput extends OutputBase implements ActionsTrainer {
    private static final int TRAINER_PORT = 6001;
    private static Logger logger = Logger.getLogger(TrainerOutput.class);
    private Trainer trainer;



    public TrainerOutput(Trainer trainer, String teamName) {
        super(TRAINER_PORT, "localhost", new CmdParserTrainer(new StringReader("")), trainer.getInput());
        this.trainer = trainer;
    }

    /**
     * Connects to the server via AbstractUDPClient.
     */
    public void connect() {
        CommandFactory f = new CommandFactory();
        f.addTrainerInitCommand();
        initMessage = f.next();
        super.start();
        super.setName("Trainer");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void changePlayMode(PlayMode playMode) {
        this.commandFactory.addChangePlayModeCommand(playMode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void movePlayer(ActionsPlayer p, double x, double y) {
        this.commandFactory.addMovePlayerCommand(p, x, y);
        String cmd = commandFactory.next();
        try {
            send(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveBall(double x, double y) {
        this.commandFactory.addMoveBallCommand(x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkBall() {
        this.commandFactory.addCheckBallCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame() {
        this.commandFactory.addStartCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void recover() {
        this.commandFactory.addRecoverCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void eye(boolean eyeOn) {
        this.commandFactory.addEyeCommand(eyeOn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ear(boolean earOn) {
        this.commandFactory.addEarCommand(earOn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void look() {
        this.commandFactory.addLookCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void teamNames() {
        this.commandFactory.addTeamNamesCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changePlayerType(String teamName, int unum, int playerType) {
        this.commandFactory.addChangePlayerTypeCommand(teamName, unum, playerType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void say(String message) {
        this.commandFactory.addSayCommand(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bye() {
        this.commandFactory.addByeCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleError(String error) {
        logger.error(error);
    }

}

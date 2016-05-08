package edu.kit.robocup.game.intf.client;


import com.github.robocup_atan.atan.model.ActionsPlayer;
import com.github.robocup_atan.atan.model.ActionsTrainer;
import com.github.robocup_atan.atan.model.enums.PlayMode;
import edu.kit.robocup.game.intf.parser.ITrainerInput;
import org.apache.log4j.Logger;

public class Trainer extends UDPClientBase implements ActionsTrainer, ITrainerInput {
    private static Logger logger = Logger.getLogger(Trainer.class);

    public Trainer(String teamName) {
        super(6001, "localhost");
        input = this;
    }

    /**
     * Connects to the server via AbstractUDPClient.
     */
    public void connect() {
        edu.kit.robocup.game.intf.parser.CommandFactory f = new edu.kit.robocup.game.intf.parser.CommandFactory();
        f.addTrainerInitCommand();
        super.start(f.next(), "Trainer");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void changePlayMode(PlayMode playMode) {
        this.commandFactory.addChangePlayModeCommand(playMode);
        sendAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void movePlayer(ActionsPlayer p, double x, double y) {
        this.commandFactory.addMovePlayerCommand(p, x, y);
        sendAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveBall(double x, double y) {
        this.commandFactory.addMoveBallCommand(x, y);
        sendAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkBall() {
        this.commandFactory.addCheckBallCommand();
        sendAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame() {
        this.commandFactory.addStartCommand();
        sendAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void recover() {
        this.commandFactory.addRecoverCommand();
        sendAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void eye(boolean eyeOn) {
        this.commandFactory.addEyeCommand(eyeOn);
        sendAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ear(boolean earOn) {
        this.commandFactory.addEarCommand(earOn);
        sendAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void look() {
        this.commandFactory.addLookCommand();
        sendAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void teamNames() {
        this.commandFactory.addTeamNamesCommand();
        sendAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changePlayerType(String teamName, int unum, int playerType) {
        this.commandFactory.addChangePlayerTypeCommand(teamName, unum, playerType);
        sendAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void say(String message) {
        this.commandFactory.addSayCommand(message);
        sendAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bye() {
        this.commandFactory.addByeCommand();
        sendAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleError(String error) {
        logger.error(error);
    }

    public void receiveGlobalVisInfo() {
        this.commandFactory.addEyeCommand(true);
        sendAll();
    }
}

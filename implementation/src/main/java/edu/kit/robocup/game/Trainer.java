package edu.kit.robocup.game;


import com.github.robocup_atan.atan.model.enums.PlayMode;
import edu.kit.robocup.game.server.client.UDPClientBase;
import org.apache.log4j.Logger;

public class Trainer extends UDPClientBase {
    private static Logger logger = Logger.getLogger(Trainer.class);

    public Trainer(String teamName) {
        super(6001, "localhost");
    }

    /**
     * Connects to the server via AbstractUDPClient.
     */
    public void connect() {
        edu.kit.robocup.game.server.message.CommandFactory f = new edu.kit.robocup.game.server.message.CommandFactory();
        f.addTrainerInitCommand();
        super.start(f.next(), "Trainer");
    }

    public void changeState(edu.kit.robocup.game.State state) {
        this.commandFactory.addMoveBallCommand(state.getBall());
    }



    public void changePlayMode(PlayMode playMode) {
        this.commandFactory.addChangePlayModeCommand(playMode);
        sendAll();
    }



    public void movePlayer(PlayerState player) {
        this.commandFactory.addMovePlayerCommand(player);
        sendAll();
    }

    public void moveBall(Ball ball) {
        this.commandFactory.addMoveBallCommand(ball);
        sendAll();
    }


    public void checkBall() {
        this.commandFactory.addCheckBallCommand();
        sendAll();
    }

    public void startGame() {
        this.commandFactory.addStartCommand();
        sendAll();
    }


    public void recover() {
        this.commandFactory.addRecoverCommand();
        sendAll();
    }

    public void eye(boolean eyeOn) {
        this.commandFactory.addEyeCommand(eyeOn);
        sendAll();
    }

    public void ear(boolean earOn) {
        this.commandFactory.addEarCommand(earOn);
        sendAll();
    }

    public void look() {
        this.commandFactory.addLookCommand();
        sendAll();
    }

    public void teamNames() {
        this.commandFactory.addTeamNamesCommand();
        sendAll();
    }

    public void changePlayerType(String teamName, int unum, int playerType) {
        this.commandFactory.addChangePlayerTypeCommand(teamName, unum, playerType);
        sendAll();
    }

    public void say(String message) {
        this.commandFactory.addSayCommand(message);
        sendAll();
    }

    public void bye() {
        this.commandFactory.addByeCommand();
        sendAll();
    }

    public void handleError(String error) {
        logger.error(error);
    }
}

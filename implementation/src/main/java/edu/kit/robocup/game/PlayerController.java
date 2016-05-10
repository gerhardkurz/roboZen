package edu.kit.robocup.game;


import com.github.robocup_atan.atan.model.enums.ViewAngle;
import com.github.robocup_atan.atan.model.enums.ViewQuality;
import edu.kit.robocup.game.server.client.StaffClientBase;
import edu.kit.robocup.game.server.message.CommandFactory;
import org.apache.log4j.Logger;

public class PlayerController extends StaffClientBase implements IPlayer, IPlayerController {
    private static Logger logger = Logger.getLogger(PlayerController.class);
    private int number;

    public PlayerController(Team team, int number) {
        super(team, 6000, "localhost");
        this.number = number;
    }


    /**
     * Connects to the server via AbstractUDPClient.
     *
     * @param isGoalie a boolean.
     */
    public void connect(boolean isGoalie) {
        CommandFactory f = new CommandFactory();
        f.addPlayerInitCommand(team.getTeamName(), isGoalie);
        initMessage = f.next();
        super.start();
    }

    public void reconnect() {
        CommandFactory f = new CommandFactory();
        f.addReconnectCommand(team.getTeamName(), number);
        super.start(f.next(), team.getTeamName() + " " + number);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        throw new Error("PlayerOutput should not use start. Use connect() instead");
    }


    public void dash(int power) {
        this.commandFactory.addDashCommand(power);
        sendAll();
    }

    public void kick(int power, double direction) {
        this.commandFactory.addKickCommand(power, (int) direction);
        sendAll();
    }

    public void move(int x, int y) {
        this.commandFactory.addMoveCommand(x, y);
        sendAll();
    }

    public void say(String message) {
        this.commandFactory.addSayCommand(message);
        sendAll();
    }

    public void turn(double angle) {
        this.commandFactory.addTurnCommand((int) angle);
        sendAll();
    }

    public void turnNeck(double angle) {
    }

    public void catchBall(double direction) {
        this.commandFactory.addCatchCommand((int) direction);
        sendAll();
    }

    public void changeViewMode(ViewQuality quality, ViewAngle angle) {
        this.commandFactory.addChangeViewCommand(angle, quality);
        sendAll();
    }

    public void bye() {
        this.commandFactory.addByeCommand();
        sendAll();
    }


    public void setNumber(int number) {
        this.number = number;
        super.setName(team.getTeamName() + " " + getNumber());
    }

    public int getNumber() {
        return number;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toStateString() {
        return super.toStateString() +
                "Team Name: " + team.getTeamName() +
                "\n" +
                "Number: " + this.getNumber() +
                "\n" +
                "Running: " + isRunning() +
                "\n" +
                "ControllerPlayer Class: " + getClass().getName() +
                "\n";
    }

    public void handleError(String error) {
        logger.error(error);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getDescription() {
        StringBuilder nam = new StringBuilder(team.getTeamName());
        nam.append(" ");
        if (number >= 0) {
            nam.append(number);
        } else {
            nam.append("<undefined>");
        }
        return nam.toString();
    }

    @Override
    public boolean hasNumber(int number) {
        return this.number == number;
    }

}

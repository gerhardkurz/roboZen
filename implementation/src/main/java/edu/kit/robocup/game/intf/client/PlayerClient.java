package edu.kit.robocup.game.intf.client;


import com.github.robocup_atan.atan.model.ActionsPlayer;
import com.github.robocup_atan.atan.model.enums.ViewAngle;
import com.github.robocup_atan.atan.model.enums.ViewQuality;
import edu.kit.robocup.game.intf.parser.CommandFactory;
import edu.kit.robocup.game.intf.parser.IPlayerInput;
import org.apache.log4j.Logger;

public class PlayerClient extends StaffBase implements ActionsPlayer, IPlayerInput {
    private static Logger logger = Logger.getLogger(PlayerClient.class);

    private boolean isTeamEast;
    private int number;

    public PlayerClient(Team team, int number) {
        super(team, 6000, "localhost");
        input = this;
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


    /**
     * {@inheritDoc}
     */
    @Override
    public void dash(int power) {
        this.commandFactory.addDashCommand(power);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void kick(int power, double direction) {
        this.commandFactory.addKickCommand(power, (int) direction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void move(int x, int y) {
        this.commandFactory.addMoveCommand(x, y);
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
    public void turn(double angle) {
        this.commandFactory.addTurnCommand((int) angle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void turnNeck(double angle) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void catchBall(double direction) {
        this.commandFactory.addCatchCommand((int) direction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeViewMode(ViewQuality quality, ViewAngle angle) {
        this.commandFactory.addChangeViewCommand(angle, quality);
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
    public void setNumber(int number) {
        this.number = number;
        super.setName(team.getTeamName() + " " + getNumber());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * Create a list string.
     *
     * @return A list string.
     */
    public String toListString() {
        StringBuffer buf = new StringBuffer();
        buf.append(input.getClass().getName());
        return buf.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toStateString() {
        StringBuffer buf = new StringBuffer();
        buf.append(super.toStateString());
        buf.append("Team Name: ");
        buf.append(team.getTeamName());
        buf.append("\n");
        buf.append("Number: ");
        buf.append(this.getNumber());
        buf.append("\n");
        buf.append("Running: ");
        buf.append(isRunning());
        buf.append("\n");
        buf.append("ControllerPlayer Class: ");
        buf.append(input.getClass().getName());
        buf.append("\n");
        return buf.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleError(String error) {
        logger.error(error);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getDescription() {
        StringBuffer nam = new StringBuffer(team.getTeamName());
        nam.append(" ");
        if (number >= 0) {
            nam.append(number);
        } else {
            nam.append("<undefined>");
        }
        return nam.toString();
    }
}

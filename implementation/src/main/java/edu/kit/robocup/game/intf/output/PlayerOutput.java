package edu.kit.robocup.game.intf.output;

//~--- non-JDK imports --------------------------------------------------------


import com.github.robocup_atan.atan.model.ActionsPlayer;
import com.github.robocup_atan.atan.model.enums.ViewAngle;
import com.github.robocup_atan.atan.model.enums.ViewQuality;
import com.github.robocup_atan.atan.parser.player.CmdParserPlayer;
import edu.kit.robocup.game.intf.client.Player;
import org.apache.log4j.Logger;

import java.io.StringReader;


/**
 * A simple implementation of AbstractUDPClient for Players.
 *
 * @author Atan
 */
public class PlayerOutput extends OutputBase implements ActionsPlayer {
    private static final int PLAYER_PORT = 6000;
    private static Logger logger = Logger.getLogger(PlayerOutput.class);

    private final Player player;



    public PlayerOutput(Player player, String teamName) {
        super(PLAYER_PORT, "localhost", new CmdParserPlayer(new StringReader("")), player.getInput());
        this.player = player;
    }


    /**
     * Connects to the server via AbstractUDPClient.
     *
     * @param isGoalie a boolean.
     */
    public void connect(boolean isGoalie) {
        CommandFactory f = new CommandFactory();
        f.addPlayerInitCommand(player.getTeamName(), isGoalie);
        initMessage = f.next();
        super.start();
    }

    public void reconnect() {
        CommandFactory f = new CommandFactory();
        f.addReconnectCommand(player.getTeamName(), player.getNumber());
        initMessage = f.next();
        super.start();
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
    public void setTeamEast(boolean is) {
        player.setTeamEast(is);
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
    public String getTeamName() {
        return player.getTeamName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNumber(int num) {
        player.setNumber(num);
        super.setName(player.getTeamName() + " Player # " + getNumber());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumber() {
        return player.getNumber();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTeamEast() {
        return player.isTeamEast();
    }

    /**
     * Create a list string.
     *
     * @return A list string.
     */
    public String toListString() {
        StringBuffer buf = new StringBuffer();
        buf.append(controller.getClass().getName());
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
        buf.append(this.getTeamName());
        buf.append("\n");
        buf.append("Number: ");
        buf.append(this.getNumber());
        buf.append("\n");
        buf.append("Running: ");
        buf.append(isRunning());
        buf.append("\n");
        buf.append("ControllerPlayer Class: ");
        buf.append(controller.getClass().getName());
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
        StringBuffer nam = new StringBuffer(getTeamName());
        nam.append(" ");
        if (player.getNumber() >= 0) {
            nam.append(player.getNumber());
        } else {
            nam.append("<undefined>");
        }
        return nam.toString();
    }
}

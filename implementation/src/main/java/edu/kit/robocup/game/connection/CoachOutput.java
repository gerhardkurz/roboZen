package edu.kit.robocup.game.connection;

//~--- non-JDK imports --------------------------------------------------------

import com.github.robocup_atan.atan.model.*;
import com.github.robocup_atan.atan.parser.CommandFilter;
import com.github.robocup_atan.atan.parser.Filter;
import com.github.robocup_atan.atan.parser.coach.CmdParserCoach;

import org.apache.log4j.Logger;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.StringReader;

/**
 * A simple implementation of AbstractUDPClient for Coaches.
 *
 * @author Atan
 */
public class CoachOutput extends AbstractUDPClient implements ActionsCoach {
    private static final int COACH_PORT = 6002;
    private static Logger log = Logger.getLogger(CoachOutput.class);
    private String initMessage = null;
    private final CmdParserCoach parser = new CmdParserCoach(new StringReader(""));
    private final Filter filter = new Filter();
    private final CommandFactory commandFactory = new CommandFactory();
    private final CommandBuffer cmdBuf = new CommandBuffer();
    private ControllerCoach controller;
    private boolean isTeamEast;
    private String teamName;

    /**
     * A part constructor for SServerCoach (assumes localhost:6002)
     *
     * @param teamName The team name.
     * @param o        a {@link com.github.robocup_atan.atan.model.ControllerCoach} object.
     */
    public CoachOutput(String teamName, ControllerCoach o) {
        this(teamName, o, COACH_PORT, "localhost");
    }

    /**
     * The full constructor for SServerCoach
     *
     * @param teamName The teams name.
     * @param con      a {@link com.github.robocup_atan.atan.model.ControllerCoach} object.
     * @param port     The port to connect to.
     * @param hostname The host address.
     */
    public CoachOutput(String teamName, ControllerCoach con, int port, String hostname) {
        super(port, hostname);
        this.teamName = teamName;
        this.controller = con;
        con.setCoach(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInitMessage() {
        return initMessage;
    }

    /**
     * Connects to the server via AbstractUDPClient.
     */
    public void connect() {
        CommandFactory f = new CommandFactory();
        f.addCoachInitCommand(teamName);
        initMessage = f.next();
        super.start();
        super.setName(teamName + " Coach");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        throw new Error("Coach should not use start. Use connect() instead");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void received(String msg) throws IOException {
        try {
            if (log.isDebugEnabled()) {
                log.debug("<---'" + msg + "'");
            }
            filter.run(msg, cmdBuf);
            cmdBuf.takeStep(controller, parser, this);
            while (commandFactory.hasNext()) {
                String cmd = commandFactory.next();
                if (log.isDebugEnabled()) {
                    log.debug("--->'" + cmd + "'");
                }
                send(cmd);
                pause(50);
            }
        } catch (Exception ex) {
            log.error("Error while receiving message: " + msg + " " + ex.getMessage(), ex);
        }
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
    public void look() {
        this.commandFactory.addLookCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getTeamNames() {
        this.commandFactory.addTeamNamesCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changePlayerType(int unum, int playerType) {
        this.commandFactory.addChangePlayerTypeCommand(unum, playerType);
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
    public void teamGraphic(XPMImage xpm) {
        this.commandFactory.addTeamGraphicCommand(xpm);
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
        log.error(error);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTeamEast() {
        return isTeamEast;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTeamEast(boolean is) {
        this.isTeamEast = is;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTeamName() {
        return teamName;
    }

    /**
     * Pause the thread.
     *
     * @param ms How long to pause the thread for (in ms).
     */
    private synchronized void pause(int ms) {
        try {
            this.wait(ms);
        } catch (InterruptedException ex) {
            log.warn("Interrupted Exception ", ex);
        }
    }
}

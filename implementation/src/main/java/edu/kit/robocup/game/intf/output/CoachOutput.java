package edu.kit.robocup.game.intf.output;

//~--- non-JDK imports --------------------------------------------------------

import com.github.robocup_atan.atan.model.*;
import com.github.robocup_atan.atan.parser.coach.CmdParserCoach;

import edu.kit.robocup.game.intf.client.Coach;
import org.apache.log4j.Logger;

//~--- JDK imports ------------------------------------------------------------

import java.io.StringReader;

/**
 * A simple implementation of AbstractUDPClient for Coaches.
 *
 * @author Atan
 */
public class CoachOutput extends OutputBase implements ActionsCoach {
    protected static final int COACH_PORT = 6002;
    private static Logger log = Logger.getLogger(CoachOutput.class);

    private Coach coach;

    public CoachOutput(Coach coach, String teamName) {
        super(COACH_PORT, "localhost", new CmdParserCoach(new StringReader("")), coach.getInput(), null);
        this.coach = coach;
    }

    /**
     * Connects to the server via AbstractUDPClient.
     */
    public void connect() {
        edu.kit.robocup.game.intf.parser.CommandFactory f = new edu.kit.robocup.game.intf.parser.CommandFactory();
        f.addCoachInitCommand(coach.getTeamName());
        super.start(f.next(), coach.getTeamName() + " Coach");
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
        return coach.isTeamEast();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTeamEast(boolean is) {
        coach.setTeamEast(is);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTeamName() {
        return coach.getTeamName();
    }


}

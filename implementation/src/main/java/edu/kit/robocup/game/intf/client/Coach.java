package edu.kit.robocup.game.intf.client;


import com.github.robocup_atan.atan.model.ActionsCoach;
import com.github.robocup_atan.atan.model.XPMImage;
import edu.kit.robocup.game.intf.parser.ICoachInput;
import org.apache.log4j.Logger;

public class Coach extends StaffBase implements ActionsCoach, ICoachInput {
    private static Logger logger = Logger.getLogger(Coach.class);

    public Coach(Team team) {
        super(team, 6002, "localhost");
        input = this;
    }

    @Override
    public void look(edu.kit.robocup.game.State state) {
        team.handleState(state);
    }

    /**
     * Connects to the server via AbstractUDPClient.
     */
    public void connect() {
        edu.kit.robocup.game.intf.parser.CommandFactory f = new edu.kit.robocup.game.intf.parser.CommandFactory();
        f.addCoachInitCommand(team.getTeamName());
        super.start(f.next(), team.getTeamName() + " Coach");
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
    public void getTeamNames() {
        this.commandFactory.addTeamNamesCommand();
        sendAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changePlayerType(int unum, int playerType) {
        this.commandFactory.addChangePlayerTypeCommand(unum, playerType);
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
    public void teamGraphic(XPMImage xpm) {
        this.commandFactory.addTeamGraphicCommand(xpm);
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

}

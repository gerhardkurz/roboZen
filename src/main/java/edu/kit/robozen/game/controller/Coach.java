package edu.kit.robozen.game.controller;


import com.github.robocup_atan.atan.model.XPMImage;
import edu.kit.robozen.game.PlayMode;
import edu.kit.robozen.game.server.client.StaffClientBase;
import org.apache.log4j.Logger;

public class Coach extends StaffClientBase {
    private PlayMode playMode = PlayMode.UNKNOWN;
    private static Logger logger = Logger.getLogger(Coach.class);

    public Coach(Team team) {
        super(team, 6002, "localhost");
    }

    public void look(edu.kit.robozen.game.state.State state) {
        state.setPlayMode(playMode);
        team.handleState(state);
    }

    public void hear(PlayMode playMode) {
        this.playMode = playMode;
    }


    /**
     * Connects to the server via AbstractUDPClient.
     */
    public void connect() {
        edu.kit.robozen.game.server.message.CommandFactory f = new edu.kit.robozen.game.server.message.CommandFactory();
        f.addCoachInitCommand(team.getPitchSide().toString());
        super.start(f.next(), team.getPitchSide().toString() + " Coach");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        throw new Error("Coach should not use start. Use connect() instead");
    }

    public void eye(boolean eyeOn) {
        this.commandFactory.addEyeCommand(eyeOn);
        sendAll();
    }

    public void bye() {
        this.commandFactory.addByeCommand();
        sendAll();
    }

    public void look() {
        this.commandFactory.addLookCommand();
        sendAll();
    }


    public void getTeamNames() {
        this.commandFactory.addTeamNamesCommand();
        sendAll();
    }


    public void changePlayerType(int unum, int playerType) {
        this.commandFactory.addChangePlayerTypeCommand(unum, playerType);
        sendAll();
    }


    public void say(String message) {
        this.commandFactory.addSayCommand(message);
        sendAll();
    }


    public void teamGraphic(XPMImage xpm) {
        this.commandFactory.addTeamGraphicCommand(xpm);
        sendAll();
    }

    public void handleError(String error) {
        logger.error(error);
    }


    public void init() {
        eye(true);
    }


}

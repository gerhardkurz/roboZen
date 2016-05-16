package edu.kit.robocup.game.controller;


import com.github.robocup_atan.atan.model.XPMImage;
import edu.kit.robocup.GameRecorder;
import edu.kit.robocup.game.server.client.StaffClientBase;
import org.apache.log4j.Logger;

public class Coach extends StaffClientBase {
    private static Logger logger = Logger.getLogger(Coach.class);
    private boolean record = false;

    public Coach(Team team) {
        super(team, 6002, "localhost");
    }

    public void look(edu.kit.robocup.game.state.State state) {
        if (record) {
            GameRecorder.record(state);
        }
        team.handleState(state);
    }

    public void recordGame(boolean record) {
        this.record = record;
    }

    /**
     * Connects to the server via AbstractUDPClient.
     */
    public void connect() {
        edu.kit.robocup.game.server.message.CommandFactory f = new edu.kit.robocup.game.server.message.CommandFactory();
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

    public void eye(boolean eyeOn) {
        this.commandFactory.addEyeCommand(eyeOn);
        sendAll();
    }

    public void bye() {
        this.commandFactory.addByeCommand();
        sendAll();
    }
/*
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

*/

}

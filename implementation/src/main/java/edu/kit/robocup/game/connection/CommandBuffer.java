package edu.kit.robocup.game.connection;

import com.github.robocup_atan.atan.model.*;
import com.github.robocup_atan.atan.parser.CommandFilter;
import com.github.robocup_atan.atan.parser.coach.CmdParserCoach;
import com.github.robocup_atan.atan.parser.player.CmdParserPlayer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class CommandBuffer implements CommandFilter {
    private String changePlayerTypeCommand = null;
    private String errorCommand = null;
    private String hearCommand = null;
    private String initCommand = null;
    private String okCommand = null;
    private String playerParamCommand = null;
    private String playerTypeCommand = null;
    private String seeCommand = null;
    private String serverParamCommand = null;
    private String warningCommand = null;

    /**
     * @inheritDoc
     */
    @Override
    public void seeCommand(String cmd) {
        seeCommand = cmd;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void hearCommand(String cmd) {
        hearCommand = cmd;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void initCommand(String cmd) {
        initCommand = cmd;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void errorCommand(String cmd) {
        errorCommand = cmd;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void serverParamCommand(String cmd) {
        serverParamCommand = cmd;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void playerParamCommand(String cmd) {
        playerParamCommand = cmd;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void playerTypeCommand(String cmd) {
        playerTypeCommand = cmd;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void changePlayerTypeCommand(String cmd) {
        changePlayerTypeCommand = cmd;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void okCommand(String cmd) {
        okCommand = cmd;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void warningCommand(String cmd) {
        warningCommand = cmd;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void senseBodyCommand(String cmd) {
    }

    private void executeMethod(Object parser, String methodName, String cmd, Object controller, Object actions) {
        for (Method method: parser.getClass().getMethods()) {
            if (method.getName().equals(methodName)) {
                try {
                    method.invoke(parser, cmd, controller, actions);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void takeStep(Object controller, Object parser, Object actions) throws Exception {

        if (seeCommand != null) {
            executeMethod(parser, "parseSeeCommand", seeCommand, controller, actions);
            seeCommand = null;
        }
        if (hearCommand != null) {
            executeMethod(parser, "parseHearCommand", hearCommand, controller, actions);
            hearCommand = null;
        }
        if (initCommand != null) {
            executeMethod(parser, "parseInitCommand", initCommand, controller, actions);
            initCommand = null;
        }
        if (okCommand != null) {
            executeMethod(parser, "parseOkCommand", okCommand, controller, actions);
            okCommand = null;
        }
        if (warningCommand != null) {
            executeMethod(parser, "parseWarningCommand", warningCommand, controller, actions);
            warningCommand = null;
        }
        if (serverParamCommand != null) {
            executeMethod(parser, "parseServerParamCommand", serverParamCommand, controller, actions);
            serverParamCommand = null;
        }
        if (playerParamCommand != null) {
            executeMethod(parser, "parsePlayerParamCommand", playerParamCommand, controller, actions);
            playerParamCommand = null;
        }
        if (playerTypeCommand != null) {
            executeMethod(parser, "parsePlayerTypeCommand", playerTypeCommand, controller, actions);
            playerTypeCommand = null;
        }
        if (changePlayerTypeCommand != null) {
            executeMethod(parser, "parseChangePlayerTypeCommand", changePlayerTypeCommand, controller, actions);
            changePlayerTypeCommand = null;
        }
        if (errorCommand != null) {
            executeMethod(parser, "parseErrorCommand", errorCommand, controller, actions);
            errorCommand = null;
        }
    }
}

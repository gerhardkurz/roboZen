package edu.kit.robocup.game.intf.output;

import com.github.robocup_atan.atan.model.AbstractUDPClient;
import com.github.robocup_atan.atan.parser.Filter;
import edu.kit.robocup.game.intf.parser.*;
import org.apache.log4j.Logger;

import java.io.IOException;


public class OutputBase extends AbstractUDPClient {
    private static Logger logger = Logger.getLogger(OutputBase.class);
    protected final CommandFactory commandFactory = new CommandFactory();

    protected IInput input;
    protected String initMessage = null;


    public OutputBase(int port, String hostname, IInput input) {
        super(port, hostname);
        this.input = input;
    }

    @Override
    public String getInitMessage() {
        return initMessage;
    }


    private void handleLook(String msg) {
        if (ICoachInput.class.isAssignableFrom(input.getClass())) {
            ICoachInput coachInput = (ICoachInput) input;
            coachInput.look(LookParser.parse(msg));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void received(String msg) throws IOException {
        log(msg, true);
        if (msg.startsWith("(ok look ")) {
            handleLook(msg);
        }
    }

    protected void sendAll() {
        while (commandFactory.hasNext()) {
            String cmd = commandFactory.next();
            try {
                log(cmd, false);
                send(cmd);
                pause(50);
            } catch (Exception ex) {
                logger.error("Error while sending command: " + cmd + " " + ex.getMessage(), ex);
            }
        }
    }

    protected void start(String initMessage, String name) {
        this.initMessage = initMessage;
        logger.info("---> " + initMessage);
        super.start();
        super.setName(name);
    }

    /**
     * Pause the thread.
     *
     * @param ms How long to pause the thread for (in ms).
     */
    protected synchronized void pause(int ms) {
        try {
            this.wait(ms);
        } catch (InterruptedException ex) {
            logger.warn("Interrupted Exception ", ex);
        }
    }

    private void log(String msg, boolean input) {
        msg = (input ? "<---" : "--->") + " " + msg;
        if (input && !(this instanceof TrainerOutput)) {
            logger.debug(msg);
        } else {
            logger.info(msg);
        }
    }
}

package edu.kit.robozen.game.server.client;

import edu.kit.robozen.game.controller.Coach;
import edu.kit.robozen.game.controller.Trainer;
import edu.kit.robozen.game.server.message.*;
import org.apache.log4j.Logger;

import java.io.IOException;


public abstract class UDPClientBase extends AbstractUDPClient {
    private static Logger logger = Logger.getLogger(UDPClientBase.class);
    protected final CommandFactory commandFactory = new CommandFactory();

    protected String initMessage = null;


    public UDPClientBase(int port, String hostname) {
        super(port, hostname);
    }


    @Override
    public String getInitMessage() {
        return initMessage;
    }




    /**
     * {@inheritDoc}
     */
    @Override
    public void received(String msg) throws IOException {
        log(msg, true);
        Parser.parse(this, msg);
    }

    protected void sendAll() {
        while (commandFactory.hasNext()) {
            String cmd = commandFactory.next();
            try {
                log(cmd, false);
                send(cmd);
            } catch (Exception ex) {
                logger.error("Error while sending command: " + cmd + " " + ex.getMessage(), ex);
            }
        }
    }

    protected void start(String initMessage, String name) {
        this.initMessage = initMessage;
        log(initMessage, false);
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
        if (input && !(this instanceof Trainer) && !(this instanceof Coach)) {
            logger.debug(msg);
        } else {
            logger.debug(msg);
        }
    }
}

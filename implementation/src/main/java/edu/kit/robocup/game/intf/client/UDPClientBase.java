package edu.kit.robocup.game.intf.client;

import com.github.robocup_atan.atan.model.AbstractUDPClient;
import edu.kit.robocup.game.intf.parser.*;
import org.apache.log4j.Logger;

import java.io.IOException;


public abstract class UDPClientBase extends AbstractUDPClient {
    private static Logger logger = Logger.getLogger(UDPClientBase.class);
    protected final CommandFactory commandFactory = new CommandFactory();

    protected IInput input;
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
        Parser.parse(input, msg);
    }

    protected void sendAll() {
        while (commandFactory.hasNext()) {
            String cmd = commandFactory.next();
            try {
                log(cmd, false);
                send(cmd);
                pause(50);
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

    private void log(String msg, boolean incoming) {
        msg = (incoming ? "<---" : "--->") + " " + msg;
        if (incoming && !(this instanceof Trainer)) {
            logger.debug(msg);
        } else {
            logger.info(msg);
        }
    }
}

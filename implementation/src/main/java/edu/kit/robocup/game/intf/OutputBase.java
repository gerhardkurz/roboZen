package edu.kit.robocup.game.intf;

import com.github.robocup_atan.atan.model.AbstractUDPClient;
import com.github.robocup_atan.atan.parser.Filter;
import org.apache.log4j.Logger;

import java.io.IOException;


public class OutputBase extends AbstractUDPClient {
    private static Logger logger = Logger.getLogger(AbstractUDPClient.class);
    protected final Filter filter = new Filter();
    protected final CommandFactory commandFactory = new CommandFactory();
    protected final CommandBuffer cmdBuf = new CommandBuffer();

    protected final Object parser;
    protected Object controller;

    protected String initMessage = null;


    public OutputBase(int port, String hostname, Object parser, Object controller) {
        super(port, hostname);
        this.parser = parser;
        this.controller = controller;
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
        try {
            logger.debug("<---'" + msg + "'");
            filter.run(msg, cmdBuf);
            cmdBuf.takeStep(controller, parser, this);
            while (commandFactory.hasNext()) {
                String cmd = commandFactory.next();
                logger.debug("--->'" + cmd + "'");
                send(cmd);
                pause(50);
            }
        } catch (Exception ex) {
            logger.error("Error while receiving message: " + msg + " " + ex.getMessage(), ex);
        }
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
}

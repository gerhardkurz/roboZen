package edu.kit.robocup.game.intf.output;

import com.github.robocup_atan.atan.model.AbstractUDPClient;
import com.github.robocup_atan.atan.parser.Filter;
import edu.kit.robocup.game.intf.parser.CommandBuffer;
import edu.kit.robocup.game.intf.parser.CommandFactory;
import edu.kit.robocup.game.intf.parser.IInputDummy;
import org.apache.log4j.Logger;

import java.io.IOException;


public class OutputBase extends AbstractUDPClient {
    private static Logger logger = Logger.getLogger(OutputBase.class);
    protected final Filter filter = new Filter();
    protected final CommandFactory commandFactory = new CommandFactory();
    protected final CommandBuffer cmdBuf = new CommandBuffer();

    protected final Object parser;
    protected Object input;
    protected IInputDummy inputDummy;

    protected String initMessage = null;


    public OutputBase(int port, String hostname, Object parser, Object input, IInputDummy inputDummy) {
        super(port, hostname);
        this.parser = parser;
        this.input = input;
        this.inputDummy = inputDummy;
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
            if (msg.startsWith("(see "))
            log(msg, true);
            filter.run(msg, cmdBuf);
            if (inputDummy != null) {
                cmdBuf.takeStep(inputDummy, parser, this);
                inputDummy.updateInput();
            } else {
                cmdBuf.takeStep(input, parser, this);
            }
            sendAll();
        } catch (Exception ex) {
            logger.error("Error while receiving message: " + msg + " " + ex.getMessage(), ex);
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
            logger.info(msg);
        } else {
            logger.info(msg);
        }
    }
}

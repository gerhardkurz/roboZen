package edu.kit.robocup.game.intf.output;

import com.github.robocup_atan.atan.model.AbstractUDPClient;
import com.github.robocup_atan.atan.parser.Filter;
import edu.kit.robocup.game.intf.input.CoachInput;
import edu.kit.robocup.game.intf.parser.*;
import org.apache.log4j.Logger;

import java.io.IOException;


public class OutputBase extends AbstractUDPClient {
    private static Logger logger = Logger.getLogger(OutputBase.class);
    protected final Filter filter = new Filter();
    protected final CommandFactory commandFactory = new CommandFactory();
    protected final CommandBuffer cmdBuf = new CommandBuffer();

    protected final Object parser;
    protected IInput input;
    protected IInputDummy inputDummy;

    protected String initMessage = null;


    public OutputBase(int port, String hostname, Object parser, IInput input, IInputDummy inputDummy) {
        super(port, hostname);
        this.parser = parser;
        this.input = input;
        this.inputDummy = inputDummy;
    }

    @Override
    public String getInitMessage() {
        return initMessage;
    }


    private void handleLook(String msg) {
        if (ICoachInput.class.isAssignableFrom(input.getClass())) {
            ICoachInput coachInput = (ICoachInput) input;
            coachInput.look(msg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void received(String msg) throws IOException {
        try {
            log(msg, true);
            if (msg.startsWith("(ok look ")) {
                handleLook(msg);
                return;
            }
            filter.run(msg, cmdBuf);
            cmdBuf.takeStep(inputDummy, parser, this);
            inputDummy.updateInput();
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
            logger.debug(msg);
        } else {
            logger.info(msg);
        }
    }
}

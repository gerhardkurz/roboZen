package edu.kit.robocup.game.intf.input;

import edu.kit.robocup.game.State;
import edu.kit.robocup.game.intf.client.Coach;
import edu.kit.robocup.game.intf.parser.ICoachInput;
import org.apache.log4j.Logger;


public class CoachInput implements ICoachInput {
    private static final Logger logger = Logger.getLogger(CoachInput.class);
    private final Coach coach;

    public CoachInput(Coach coach) {
        this.coach = coach;
    }

    @Override
    public void look(State state) {
        logger.info(state);
    }
}

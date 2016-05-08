package edu.kit.robocup.game.intf.input;


import com.github.robocup_atan.atan.model.ActionsCoach;
import com.github.robocup_atan.atan.model.ActionsPlayer;
import com.github.robocup_atan.atan.model.ControllerCoach;
import com.github.robocup_atan.atan.model.enums.*;
import edu.kit.robocup.game.intf.client.Coach;
import edu.kit.robocup.game.intf.parser.ICoachInput;
import org.apache.log4j.Logger;

import java.util.HashMap;

public class CoachInput implements ICoachInput {
    private static final Logger logger = Logger.getLogger(CoachInput.class);
    private final Coach coach;

    public CoachInput(Coach coach) {
        this.coach = coach;
    }

    @Override
    public void look(String msg) {
        logger.info(msg);
    }
}

package edu.kit.robocup.game.connection;


import com.github.robocup_atan.atan.model.ControllerTrainer;
import com.github.robocup_atan.atan.model.enums.*;
import org.apache.log4j.Logger;

public class TrainerInput implements ControllerTrainer {
    private static Logger logger = Logger.getLogger(TrainerInput.class);

    // DISCLAIMER METHODS ARE ____NEVER____ CALLED!
    @Override
    public void infoHearPlayer(double direction, String message) {
        logger.info(message);
    }

    @Override
    public void infoHearPlayMode(PlayMode playMode) {
        logger.info(playMode);
    }

    @Override
    public void infoHearReferee(RefereeMessage refereeMessage) {
        logger.info(refereeMessage);
    }

    @Override
    public void infoHearError(Errors error) {
        logger.error(error);
    }

    @Override
    public void infoHearOk(Ok ok) {
        logger.info(ok);
    }

    @Override
    public void infoHearWarning(Warning warning) {
        logger.warn(warning);
    }
}

package edu.kit.robocup.util;


import edu.kit.robocup.game.PlayMode;
import edu.kit.robocup.interf.mdp.IState;

public class EndGameStatusSupplier<T> extends BooleanStatusSupplier<T> {
    public EndGameStatusSupplier(Class booleanClass) {
        super(booleanClass);
    }

    @Override
    protected boolean getStatus(IState state) {
        PlayMode playMode = state.getPlayMode();
        return  playMode != PlayMode.GOAL_SIDE_EAST && playMode != PlayMode.GOAL_SIDE_WEST;
    }
}

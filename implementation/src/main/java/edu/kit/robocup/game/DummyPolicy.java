package edu.kit.robocup.game;

import edu.kit.robocup.game.action.IAction;
import edu.kit.robocup.mdp.IPolicy;
import edu.kit.robocup.mdp.IState;

import java.util.Map;


public class DummyPolicy implements IPolicy {
    @Override
    public Map<PlayerState, IAction> getAction(IState state) {
        state.getPlayers("t1");
        return null;
    }
}

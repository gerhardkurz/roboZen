package edu.kit.robocup.example.policy;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.PlayMode;
import edu.kit.robocup.game.controller.IPlayerController;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.interf.mdp.IState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PerPlayModePolicy implements IPolicy {
    private final IPolicy defaultPolicy;
    private final Map<PlayMode, IPolicy> policiesPerPlaymode = new HashMap<>();

    public PerPlayModePolicy(IPolicy defaultPolicy) {
        this.defaultPolicy = defaultPolicy;
    }


    @Override
    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> playerControllers, PitchSide pitchSide) {
        if (policiesPerPlaymode.containsKey(state.getPlayMode())) {
            return policiesPerPlaymode.get(state.getPlayMode()).apply(state, playerControllers, pitchSide);
        }
        return defaultPolicy.apply(state, playerControllers, pitchSide);
    }

    public void replacePolicyForPlayMode(IPolicy policy, PlayMode... playModes) {
        for (PlayMode playMode: playModes) {
            policiesPerPlaymode.put(playMode, policy);
        }
    }
}

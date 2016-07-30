package edu.kit.robozen.util;


import edu.kit.robozen.constant.PitchSide;
import edu.kit.robozen.game.controller.IPlayerController;
import edu.kit.robozen.interf.game.IAction;
import edu.kit.robozen.interf.mdp.IPolicy;
import edu.kit.robozen.interf.mdp.IState;

import java.util.List;
import java.util.Map;

public class StatusPolicy<T> implements IPolicy {
    private IPolicy policy;
    private final StatusSupplier<T> statusSupplier;

    public StatusPolicy(StatusSupplier<T> statusSupplier) {
        this.statusSupplier = statusSupplier;
    }

    public T getStatus() {
        return statusSupplier.getStatus();
    }

    public void setPolicy(IPolicy policy) {
        this.policy = policy;
    }

    @Override
    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> playerControllers, PitchSide pitchSide) {
        statusSupplier.updateStatus(state);
        return policy.apply(state, playerControllers, pitchSide);
    }
}

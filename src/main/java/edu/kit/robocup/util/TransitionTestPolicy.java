package edu.kit.robocup.util;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.game.controller.IPlayerController;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.interf.mdp.IState;
import edu.kit.robocup.mdp.PlayerActionSet;
import edu.kit.robocup.mdp.transition.ITransition;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;


public class TransitionTestPolicy implements IPolicy {
    private static final Logger logger = Logger.getLogger(TransitionTestPolicy.class.getName());
    private final IPolicy policy;
    private final List<ITransition> transitions;

    private IState previousState = null;
    private PlayerActionSet previousPlayerActionSet = null;

    public TransitionTestPolicy(IPolicy policy, ITransition... transitions) {
        this.policy = policy;
        this.transitions = Arrays.asList(transitions);
    }

    public void evaluatePrediction(IState actual, IState predicted) {
        logger.info("act: " + actual.getPlayers(PitchSide.EAST));
        logger.info("pred: "+ predicted.getPlayers(PitchSide.EAST));

        logger.info("act: " + actual.getBall());
        logger.info("pred: " + predicted.getBall());
        //logger.info("act: " +actual.getPlayers(PitchSide.WEST).get(0));
        //logger.info("pred: " +predicted.getPlayers(PitchSide.WEST).get(0));
    }

    @Override
    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> playerControllers, PitchSide pitchSide) {
        Map<IPlayerController, IAction>  actions = policy.apply(state, playerControllers, pitchSide);
        ArrayList<PlayerAction> playerActions = actions.entrySet().stream().map(e -> new PlayerAction(e.getKey().getNumber(), e.getValue())).collect(Collectors.toCollection(ArrayList::new));
        PlayerActionSet playerActionSet = new PlayerActionSet(playerActions);
        if (previousState != null) {
            for (ITransition transition: transitions) {
                evaluatePrediction(state, transition.getNewStateSample((State) previousState, previousPlayerActionSet, pitchSide));
            }
        }
        previousState = state;
        previousPlayerActionSet = playerActionSet;
        return actions;
    }
}

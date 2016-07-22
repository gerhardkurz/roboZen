package edu.kit.robocup.recorder;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.PlayMode;
import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.game.controller.IPlayerController;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.interf.mdp.IState;
import edu.kit.robocup.mdp.PlayerActionSet;
import org.apache.log4j.Logger;

public class GameRecorder implements IPolicy {
    public static final String fileEnding = ".gl";
    private static final Logger logger = Logger.getLogger(GameRecorder.class.getName());
    private IPolicy policy;
    private ObjectOutputStream oos;
    private boolean gameStarted = false;

    public GameRecorder(String fileName, IPolicy policy) {
        File file = new File(fileName + fileEnding);
        this.policy = policy;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            this.oos = new ObjectOutputStream(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void record(Serializable stateOrAction) {
        try {
            logger.info("recording: " + stateOrAction);
            this.oos.writeObject(stateOrAction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void endRecording() {
        try {
            this.oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> playerControllers, PitchSide pitchSide) {
        Map<IPlayerController, IAction> actions = policy.apply(state, playerControllers, pitchSide);

        if (state.getPlayMode() == PlayMode.PLAY_ON) {
            gameStarted = true;
        }
        if (gameStarted) {
            record(state);
            ArrayList<PlayerAction> playerActions = actions.entrySet().stream().map(e -> new PlayerAction(e.getKey().getNumber(), e.getValue())).collect(Collectors.toCollection(ArrayList::new));
            record(new PlayerActionSet(playerActions));
        }
        return actions;
    }
}

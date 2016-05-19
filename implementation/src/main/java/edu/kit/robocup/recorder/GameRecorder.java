package edu.kit.robocup.recorder;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.game.controller.IPlayerController;

import java.io.*;
import java.util.List;
import java.util.Map;

import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.interf.mdp.IState;
import org.apache.log4j.Logger;

public class GameRecorder implements IPolicy {
    public static final String fileEnding = ".gl";
    static Logger logger = Logger.getLogger(GameRecorder.class.getName());
    private IPolicy policy;
    FileOutputStream fos;
    ObjectOutputStream oos;

    private File file;
    public GameRecorder(String fileName, IPolicy policy) {
        this.file = new File(fileName + fileEnding);
        this.policy = policy;
        try {
            this.fos = new FileOutputStream(this.file);
            this.oos = new ObjectOutputStream(this.fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void record(Serializable stateOrAction) {
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
        record(state);
        Map<IPlayerController, IAction> actions = policy.apply(state, playerControllers, pitchSide);
        //record(actions);
        for (Map.Entry<IPlayerController, IAction> entry : actions.entrySet())
        {
            record(entry.getValue());
            logger.info("recording: Player " + entry.getKey().getNumber() + " " + entry.getValue());
        }
        return actions;
    }
}

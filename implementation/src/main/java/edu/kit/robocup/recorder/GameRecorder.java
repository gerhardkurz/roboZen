package edu.kit.robocup.recorder;

import edu.kit.robocup.game.controller.Coach;
import edu.kit.robocup.game.server.message.CommandFactory;
import edu.kit.robocup.game.state.State;

import java.io.*;

import org.apache.log4j.Logger;

public class GameRecorder {
    public static final String fileEnding = ".gl";
    static Logger logger = Logger.getLogger(GameRecorder.class.getName());
    FileOutputStream fos;
    ObjectOutputStream oos;

    private File file;
    public GameRecorder(String fileName, Coach observer) {
        this.file = new File(fileName + fileEnding);

        try {
            this.fos = new FileOutputStream(this.file);
            this.oos = new ObjectOutputStream(this.fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        observer.recordStates(this);
        CommandFactory.recordActions(this);
    }

    public void record(Serializable stateOrAction) {
        try {
            logger.info(stateOrAction);
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


}

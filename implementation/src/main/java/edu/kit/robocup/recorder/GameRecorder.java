package edu.kit.robocup.recorder;

import edu.kit.robocup.game.controller.Coach;
import edu.kit.robocup.game.state.State;

import java.io.*;

import org.apache.log4j.Logger;

public class GameRecorder {

    static Logger logger = Logger.getLogger(GameRecorder.class.getName());
    FileOutputStream fos;
    ObjectOutputStream oos;

    private File file;
    public GameRecorder(String file, Coach observer) {
        this.file = new File(file);

        try {
            this.fos = new FileOutputStream(file);
            this.oos = new ObjectOutputStream(this.fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        observer.recordGame(this);
    }

    public void record(State state) {
        try {
            //logger.info(state);
            this.oos.writeObject(state);
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

package edu.kit.robocup.recorder;

import edu.kit.robocup.game.state.State;
import edu.kit.robocup.mdp.PlayerActionSet;
import edu.kit.robocup.mdp.transition.Game;
import org.apache.log4j.Logger;

import javax.activation.UnsupportedDataTypeException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class GameReader {
    private static Logger logger = Logger.getLogger(GameReader.class.getName());

    public static List<Game> getGamesFromFiles(String... fileNames) {
        List<Game> games = new ArrayList<>();
        for (String filename : fileNames) {
            GameReader gr = new GameReader(filename);
            gr.addGamesFromFile(games);
        }
        return games;
    }

    private File file;
    public GameReader(String fileName) {
        this.file = new File(fileName + GameRecorder.fileEnding);
    }

    private void addGamesFromFile(List<Game> games) {
        try {
            FileInputStream fileInputStream = new FileInputStream(this.file);
            ObjectInputStream input = new ObjectInputStream(fileInputStream);

            State previousState = null;
            PlayerActionSet playerActionSet = null;
            boolean nextIsState = true;
            while (fileInputStream.available() > 0) {
                Object readObject = input.readObject();
                if (nextIsState && !State.class.isAssignableFrom(readObject.getClass()) || !nextIsState && !PlayerActionSet.class.isAssignableFrom(readObject.getClass())) {
                    throw new UncheckedIOException(new UnsupportedDataTypeException("File contents should alternate between state and actions objects. Starting with a state object."));
                }
                if (nextIsState) {
                    State state = (State) readObject;
                    if (previousState != null) {
                        games.add(new Game(previousState, playerActionSet, state));
                    }
                    previousState = state;
                } else {
                    playerActionSet = (PlayerActionSet) readObject;
                }
                nextIsState = !nextIsState;
            }

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

}

package edu.kit.robocup.recorder;

import edu.kit.robocup.game.IAction;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.mdp.ActionSet;
import edu.kit.robocup.mdp.IActionSet;
import edu.kit.robocup.mdp.transitions.Game;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameReader {
    static Logger logger = Logger.getLogger(GameReader.class.getName());

    public static List<Game> getGamesFromFiles(List<String> fileNames) {
        List<Game> games = new ArrayList<>();
        for (String filename : fileNames) {
            GameReader gr = new GameReader(filename);
            games.add(gr.getGameFromFile());
        }
        return games;
    }

    private File file;
    public GameReader(String fileName) {
        this.file = new File(fileName + GameRecorder.fileEnding);
    }

    public Game getGameFromFile() {
        List<State> states = new ArrayList<>();
        List<IActionSet> actionSets = new ArrayList<>();
        List<IAction> actions = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(this.file);
            ObjectInputStream input = new ObjectInputStream(fileInputStream);
            int stateCount = 0;
            int actionCount = 0;
            try {
                while (true) {
                    Object readObject = input.readObject();
                    if (readObject.getClass().equals(State.class)) {
                        if (!actions.isEmpty()) {
                            ActionSet actionSet = new ActionSet(actions);
                            actionSets.add(actionSet);
                            actions = new ArrayList<>();
                        }
                        stateCount++;
                        states.add((State) readObject);
                    } else {
                        actionCount++;
                        actions.add((IAction) readObject);
                    }
                }
            } catch (EOFException eof) {
                if (!actions.isEmpty()) {
                    ActionSet actionSet = new ActionSet(actions);
                    actionSets.add(actionSet);
                    actions.clear();
                }

                input.close();
                logger.info("File (" + file + ") was read (" + stateCount + " states, " + actionCount + " actions) and closed successfully.");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new Game(states, actionSets, actionSets.get(0).getActions().size());
    }

}

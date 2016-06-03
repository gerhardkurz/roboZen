package edu.kit.robocup.recorder;

import edu.kit.robocup.game.PlayerAction;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.mdp.ActionSet;
import edu.kit.robocup.interf.mdp.IActionSet;
import edu.kit.robocup.mdp.PlayerActionSet;
import edu.kit.robocup.mdp.transition.Game;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.javacc.parser.LexGen.actions;

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
        List<PlayerActionSet> playerActionSets = new ArrayList<>();
        List<PlayerAction> playerActions = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(this.file);
            ObjectInputStream input = new ObjectInputStream(fileInputStream);
            int stateCount = 0;
            int actionCount = 0;
            try {
                while (true) {
                    Object readObject = input.readObject();
                    if (readObject.getClass().equals(State.class)) {
                        if (!playerActions.isEmpty()) {
                            PlayerActionSet actionSet = new PlayerActionSet(playerActions);
                            playerActionSets.add(actionSet);
                            playerActions = new ArrayList<>();
                        }
                        stateCount++;
                        states.add((State) readObject);
                    } else {
                        actionCount++;
                        playerActions.add((PlayerAction) readObject);
                    }
                }
            } catch (EOFException eof) {
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

        return new Game(states, playerActionSets);
    }

}

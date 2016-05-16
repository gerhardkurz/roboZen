package edu.kit.robocup.recorder;

import com.github.robocup_atan.atan.model.enums.PlayMode;
import edu.kit.robocup.Util;
import edu.kit.robocup.game.controller.Coach;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.game.controller.Team;
import edu.kit.robocup.game.controller.Trainer;
import edu.kit.robocup.game.state.State;
import edu.kit.robocup.mdp.SimplePolicy;
import edu.kit.robocup.mdp.transitions.Game;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class GameRecorder {

    static Logger logger = Logger.getLogger(GameRecorder.class.getName());
    static FileOutputStream fos;
    static ObjectOutputStream oos;

    public static void main(String[] args) throws IOException {
        initEnvironment();

        /*
        * Connect desired teams here
        */
        Trainer trainer = new Trainer("Trainer");
        trainer.connect();

        Team team1 = new Team("t1", 1, new SimplePolicy());
        team1.connectAll();

        Team team2 = new Team("t2", 2, new SimplePolicy());
        team2.connectAll();

        trainer.movePlayer(new PlayerState("t1", 1, 20, 20));
        trainer.movePlayer(new PlayerState("t2", 1, 20, 20));

        trainer.moveBall(new Ball(3, 3));




        startRecording("test.tmp", team1.getCoach());
        team1.getCoach().eye(true); // enables constant visual updates for trainer/coach
        trainer.changePlayMode(PlayMode.KICK_OFF_L);

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                endRecording();
            }
        });
    }

    private static void initEnvironment() {
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        Util.startServer();
        Util.startMonitor();
    }

    public static void startRecording(String file, Coach observer) {
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        observer.recordGame(true);
    }

    public static void record(State state) {
        try {
            //logger.info(state);
            oos.writeObject(state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void endRecording() {
        try {
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Game getGameFromFile(String file) {
        //Game game;


        return null;
    }

    private static List<State> getStatesFromFile() {
        List<State> states = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(file));
            ObjectInputStream input = new ObjectInputStream(fileInputStream);
            State state;
            try {
                while (true) {
                    state = (State) input.readObject();
                    states.add(state);
                }
            } catch (EOFException eof) {
                input.close();
                logger.info("File was read and closed: " + file);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

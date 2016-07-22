package edu.kit.robocup.util;


import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.SimpleGameObject;
import edu.kit.robocup.game.controller.Team;
import edu.kit.robocup.game.controller.Trainer;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.interf.mdp.IPolicy;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;



public class Util {
    private Util() {
    }

    public static final String rcssDir = "..\\rcss\\";


    public static void initEnvironment() {
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        Util.startServer();
        Util.startMonitor();
    }

    public static void startServer() {
        System.out.println("starting rcssserver!");
        killAndStart("rcssserver.exe", rcssDir + "rcssserver-14.0.3-win\\rcssserver.exe");
    }

    public static void startMonitor() {
        System.out.println("starting rcssmonitor!");
        killAndStart("rcssmonitor.exe", rcssDir + "rcssmonitor-14.1.0-win\\rcssmonitor.exe");
    }

    public static void killAndStart(String processName, String path) {
        killTask(processName);
        try {
            TimeUnit.MILLISECONDS.sleep(500);
            startExe(path);
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void startExe(String path) {
        try {
            File file = new File(path).getAbsoluteFile();
            ProcessBuilder pb = new ProcessBuilder(file.getAbsolutePath());
            File dir = file.getParentFile().getAbsoluteFile();
            pb.directory(dir);

            Process p = pb.start();
            printStream(p.getInputStream());
            printStream(p.getErrorStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void killTask(String processName) {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM " + processName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printStream(InputStream inputStream) throws IOException {
        final BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));

        new Thread(new Runnable() {
            public void run() {
                String line;
                try {
                    while ((line = input.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
        }).start();
    }


    public static class TeamDescription {
        private final IPolicy policy;
        private final int numberPlayers;
        private final List<SimpleGameObject> positions;

        public TeamDescription(IPolicy policy, int numberPlayers, List<SimpleGameObject> positions) {
            this.policy = policy;
            this.numberPlayers = numberPlayers;
            this.positions = positions;
        }
    }

    public static void executeGame(TeamDescription teamWest, TeamDescription teamEast, TrainerCommand trainerCommand) throws InterruptedException {
        StatusSupplier<Boolean> statusSupplierWest = new EndGameStatusSupplier<>(Boolean.class);
        StatusSupplier<Boolean> statusSupplierEast = new EndGameStatusSupplier<>(Boolean.class);
        executeGame(teamWest, teamEast,  trainerCommand, new StatusPolicy<>(statusSupplierWest), new StatusPolicy<>(statusSupplierEast));
    }

    public static void executeGame(TeamDescription teamWest, TeamDescription teamEast, TrainerCommand trainerCommand, StatusPolicy<Boolean> endGamePolicyWest, StatusPolicy<Boolean> endGamePolicyEast) throws InterruptedException {
        Util.initEnvironment();
        Trainer trainer = new Trainer("Trainer");
        trainer.connect();
        endGamePolicyWest.setPolicy(teamWest.policy);
        Team team1 = new Team(PitchSide.WEST, teamWest.numberPlayers, endGamePolicyWest);
        team1.connectAll();

        endGamePolicyEast.setPolicy(teamEast.policy);
        Team team2 = new Team(PitchSide.EAST, teamEast.numberPlayers, endGamePolicyEast);
        team2.connectAll();

        for (int i = 0; i < teamWest.positions.size(); i++) {
            SimpleGameObject position = teamWest.positions.get(i);
            trainer.movePlayer(new PlayerState(PitchSide.WEST, i + 1, position.getPositionX(), position.getPositionY()));
        }

        for (int i = 0; i < teamEast.positions.size(); i++) {
            SimpleGameObject position = teamEast.positions.get(i);
            trainer.movePlayer(new PlayerState(PitchSide.EAST, i + 1, position.getPositionX(), position.getPositionY()));
        }
        Thread.sleep(2000);
        trainerCommand.execute(trainer);
        trainer.changePlayMode(com.github.robocup_atan.atan.model.enums.PlayMode.PLAY_ON);

        while (endGamePolicyWest.getStatus() || endGamePolicyEast.getStatus()) {
            Thread.sleep(30);
        }
        //yolo
        //Thread.getAllStackTraces().keySet().stream().filter(thread -> !thread.equals(Thread.currentThread())).forEach(Thread::interrupt);
        //Util.killTask("rcssserver.exe");
        //Util.killTask("rcssmonitor.exe");
        Thread.sleep(200);
    }

    public interface TrainerCommand {
        void execute(Trainer trainer);
    }
}

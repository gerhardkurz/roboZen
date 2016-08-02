package edu.kit.robozen.util;


import edu.kit.robozen.constant.PitchSide;
import edu.kit.robozen.game.SimpleGameObject;
import edu.kit.robozen.game.controller.Team;
import edu.kit.robozen.game.controller.Trainer;
import edu.kit.robozen.game.state.PlayerState;
import edu.kit.robozen.interf.mdp.IPolicy;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;



public class Util {

    private Util() {
    }

    public static void initEnvironmentWin(InitWinConfiguration configuration) {
        PropertyConfigurator.configure(configuration.log4jConfig);
        System.out.println("starting rcssserver!");
        killAndStartWin(configuration.server, configuration.workingDirectory);
        System.out.println("starting rcssmonitor!");
        killAndStartWin(configuration.monitor, configuration.workingDirectory);
    }

    public static void killAndStartWin(String exeString, String homeString) {
        Path exePath = Paths.get(exeString).toAbsolutePath();
        Path homePath = Paths.get(homeString).toAbsolutePath();
        killTask(exePath);
        try {
            TimeUnit.MILLISECONDS.sleep(500);
            startExeWin(exePath, homePath);
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void startExeWin(Path exePath, Path homePath) {
        try {
            ProcessBuilder pb = new ProcessBuilder(exePath.toString());
            pb.directory(homePath.toFile());

            Process p = pb.start();
            printStream(p.getInputStream());
            printStream(p.getErrorStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void killTask(Path path) {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM " + path.getFileSystem().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printStream(InputStream inputStream) throws IOException {
        final BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));

        new Thread(() -> {
            String line;
            try {
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public static void executeGameWin(InitWinConfiguration configuration, TeamDescription teamWest, TeamDescription teamEast, TrainerCommand trainerCommand) throws InterruptedException {
        StatusSupplier<Boolean> statusSupplierWest = new EndGameStatusSupplier<>(Boolean.class);
        StatusSupplier<Boolean> statusSupplierEast = new EndGameStatusSupplier<>(Boolean.class);
        executeGameWin(configuration, teamWest, teamEast,  trainerCommand, new StatusPolicy<>(statusSupplierWest), new StatusPolicy<>(statusSupplierEast));
    }

    public static void executeGameWin(InitWinConfiguration configuration, TeamDescription teamWest, TeamDescription teamEast, TrainerCommand trainerCommand, StatusPolicy<Boolean> endGamePolicyWest, StatusPolicy<Boolean> endGamePolicyEast) throws InterruptedException {
        Util.initEnvironmentWin(configuration);
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

    public static class InitWinConfiguration {
        private final String workingDirectory;
        private final String log4jConfig;
        private final String server;
        private final String monitor;

        public InitWinConfiguration(String workingDirectory, String log4jConfig, String server, String monitor) {
            this.workingDirectory = workingDirectory;
            this.log4jConfig = log4jConfig;
            this.server = server;
            this.monitor = monitor;
        }
    }
}

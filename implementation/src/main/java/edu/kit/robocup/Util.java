package edu.kit.robocup;


import java.io.*;
import java.util.concurrent.TimeUnit;



public class Util {
    private Util() {
    }

    public static final String rcssDir = "..\\rcss\\";

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


}

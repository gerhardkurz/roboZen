package edu.kit.robocup;


import java.io.*;

import org.apache.log4j.Logger;


public class Sandbox {

    static Logger logger = Logger.getLogger(Sandbox.class.getName());

    public static void main(String[] args) throws IOException {

        GameRecorder.getGameFromFile("test.tmp");
    }
}

package edu.kit.robocup;

import edu.kit.robocup.util.Util;

import org.apache.log4j.Logger;

import java.io.IOException;

public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());


    public static void main(String[] args) throws IOException, InterruptedException {
        Util.initEnvironment("build/test");
    }
}

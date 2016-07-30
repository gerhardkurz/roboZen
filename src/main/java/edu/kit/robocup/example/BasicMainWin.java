package edu.kit.robocup.example;

import edu.kit.robocup.util.Util;

import org.apache.log4j.Logger;

import java.io.IOException;

public class BasicMainWin {

    static Logger logger = Logger.getLogger(BasicMainWin.class.getName());


    public static void main(String[] args) throws IOException, InterruptedException {
        Util.initEnvironmentWin("build/test");
    }
}

package com.elevators;

import com.elevators.elevator.ElevatorsController;
import com.elevators.person.PersonGenerator;
import com.elevators.util.Stats;

import java.util.logging.Logger;

public class Main {

    private static Logger logger;

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s\n");
        logger = Logger.getLogger("Logger");
        new PersonGenerator().start();
        new Stats().start();
        ElevatorsController.getController().run();
    }

    public static Logger getLogger() {
        return logger;
    }
}

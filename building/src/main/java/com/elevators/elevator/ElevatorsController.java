package com.elevators.elevator;

import com.elevators.util.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ElevatorsController implements Runnable {

    private static final ElevatorsController controller = new ElevatorsController();
    private final List<String> events = new ArrayList<>();
    private final List<Elevator> elevators = new ArrayList<>();
    private int serviced;

    private ElevatorsController() {
        for (int i = 0; i < Config.NUMBER_OF_ELEVATORS; i++) {
            elevators.add(new Elevator());
        }
        elevators.forEach(Thread::start);
    }

    public static ElevatorsController getController() {
        return controller;
    }

    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!events.isEmpty()) {
                String[] event = events.get(0).split(" ");
                for (Elevator elevator : elevators) {
                    if (elevator.getNextDestination() == 0) {
                        elevator.setNextDestination(Integer.parseInt(event[0]));
                        elevator.setUp(event[1].equals("UP"));
                        break;
                    }
                }
                events.remove(0);
            }
        }
    }

    public int addServiced() {
        return ++serviced;
    }

    public List<String> getEvents() {
        return events;
    }

    public List<Elevator> getElevators() {
        return elevators;
    }

    public int getServiced() {
        return serviced;
    }
}

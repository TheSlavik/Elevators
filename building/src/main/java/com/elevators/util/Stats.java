package com.elevators.util;

import com.elevators.elevator.Elevator;
import com.elevators.elevator.ElevatorsController;
import com.elevators.person.PersonGenerator;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Stats extends Thread {

    private static final Stats stats = new Stats();

    static {
        stats.setDaemon(true);
        stats.setName("Stats");
    }

    private static void showStats() {
        List<Elevator> elevators = ElevatorsController.getController().getElevators();
        System.out.println("\nSTATS\n");
        for (int i = 0; i < elevators.size(); i++) {
            Elevator elevator = elevators.get(i);
            String s = "Elevator %d: serviced %d people, distance - %dm."
                    .formatted(i + 1, elevator.getServicedThis(), elevator.getPath());
            System.out.println(s);
        }
        String total = "\nTotal serviced: %d/%d."
                .formatted(ElevatorsController.getController().getServiced(), PersonGenerator.getCount());
        System.out.println(total);
        System.out.println();
    }

    public void run() {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(30);
                showStats();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

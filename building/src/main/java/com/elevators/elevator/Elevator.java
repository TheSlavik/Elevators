package com.elevators.elevator;

import com.elevators.Main;
import com.elevators.building.Building;
import com.elevators.building.Floor;
import com.elevators.person.Person;
import com.elevators.person.PersonGenerator;
import com.elevators.util.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class Elevator extends Thread {

    private final int speed = Config.ELEVATOR_SPEED;

    private final int timeToGo = Config.ELEVATOR_TIME_TO_GO;
    private final List<Person> people = new ArrayList<>();
    private int capacity = Config.CAPACITY;
    private int currentFloor = 1;
    private int nextDestination = 0;
    private boolean isUp;
    private int path;
    private int servicedThis;

    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                if (!people.isEmpty()) {
                    goTo(people.get(0).to());
                } else if (nextDestination != 0) {
                    int to = nextDestination;
                    nextDestination = 0;
                    goTo(to);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void goTo(int destination) {
        try {
            if (currentFloor != destination) {
                if (currentFloor < destination) {
                    while (currentFloor != destination) {
                        TimeUnit.MILLISECONDS.sleep(speed);
                        path += 3;
                        currentFloor++;
                    }
                } else {
                    while (currentFloor != destination) {
                        TimeUnit.MILLISECONDS.sleep(speed);
                        path += 3;
                        currentFloor--;
                    }
                }
            }
            leaveElevator();
            int count = getPeople(isUp);
            if (count == 0) {
                getPeople(!isUp);
            }
            TimeUnit.SECONDS.sleep(timeToGo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void leaveElevator() {
        List<Person> collect = people.stream().filter(x -> x.to() == currentFloor).toList();
        capacity += collect.stream().mapToInt(Person::weight).sum();
        people.removeAll(collect);
        for (int i = 0; i < collect.size(); i++) {
            servicedThis++;
            int serviced = ElevatorsController.getController().addServiced();
            Main.getLogger().log(Level.INFO,
                    "Person is serviced. (%d/%d)".formatted(serviced, PersonGenerator.getCount()));
        }
    }

    private int getPeople(boolean isUp) {
        Floor floor = Building.getBuilding().getFloors().get(currentFloor - 1);
        int count = 0;
        if (isUp) {
            while (!floor.getPeopleUp().isEmpty() && capacity >= floor.getPeopleUp().get(0).weight()) {
                people.add(floor.getPeopleUp().get(0));
                capacity -= floor.getPeopleUp().get(0).weight();
                floor.getPeopleUp().remove(0);
                count++;
            }
            if (floor.getPeopleUp().isEmpty()) {
                ElevatorsController.getController().getEvents().remove(floor.getNumber() + " UP");
            }
        } else {
            while (!floor.getPeopleDown().isEmpty() && capacity >= floor.getPeopleDown().get(0).weight()) {
                people.add(floor.getPeopleDown().get(0));
                capacity -= floor.getPeopleDown().get(0).weight();
                floor.getPeopleDown().remove(0);
                count++;
            }
            if (floor.getPeopleDown().isEmpty()) {
                ElevatorsController.getController().getEvents().remove(floor.getNumber() + " DOWN");
            }
        }
        return count;
    }

    public int getNextDestination() {
        return nextDestination;
    }

    public void setNextDestination(int nextDestination) {
        this.nextDestination = nextDestination;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public int getPath() {
        return path;
    }

    public int getServicedThis() {
        return servicedThis;
    }
}

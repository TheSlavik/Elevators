package com.elevators.building;

import com.elevators.elevator.ElevatorsController;
import com.elevators.person.Person;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Floor {

    private final int number;
    private final List<Person> peopleUp = new CopyOnWriteArrayList<>();
    private final List<Person> peopleDown = new CopyOnWriteArrayList<>();

    public Floor(int number) {
        this.number = number;
    }

    public void addPerson(Person person) {
        if (person.from() - person.to() < 0) {
            peopleUp.add(person);
            ElevatorsController.getController().getEvents().add(number + " UP");
        } else {
            peopleDown.add(person);
            ElevatorsController.getController().getEvents().add(number + " DOWN");
        }
    }

    public int getNumber() {
        return number;
    }

    public List<Person> getPeopleUp() {
        return peopleUp;
    }

    public List<Person> getPeopleDown() {
        return peopleDown;
    }
}

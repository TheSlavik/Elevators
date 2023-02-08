package com.elevators.person;

import com.elevators.Main;
import com.elevators.building.Building;
import com.elevators.util.Config;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.random.RandomGenerator;

public class PersonGenerator extends Thread {

    private static final PersonGenerator generator = new PersonGenerator();
    private static int count;

    static {
        generator.setDaemon(true);
        generator.setName("Generator");
    }

    private final RandomGenerator randomGenerator = RandomGenerator.getDefault();

    public static int getCount() {
        return count;
    }

    public void run() {
        while (isAlive()) {
            try {
                TimeUnit.SECONDS.sleep(randomGenerator.nextInt(Config.GENERATION_DELAY));
                Person person;
                int floor = randomGenerator.nextInt(Config.NUMBER_OF_FLOORS - 1) + 2;
                int weight = randomGenerator.nextInt(30, 120);
                if (randomGenerator.nextInt(100) < 60) {
                    person = randomGenerator.nextBoolean() ?
                            new Person(1, floor, weight) : new Person(floor, 1, weight);
                } else {
                    int to;
                    do {
                        to = randomGenerator.nextInt(Config.NUMBER_OF_FLOORS - 1) + 2;
                    } while (floor == to);
                    person = new Person(floor, to, weight);
                }
                Building.getBuilding().getFloors().get(person.from() - 1).addPerson(person);
                count++;
                Main.getLogger().log(Level.INFO, "New person is on the %d floor.".formatted(person.from()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

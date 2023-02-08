package com.elevators.building;

import com.elevators.util.Config;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Building {

    private static final Building building = new Building();
    private final int numOfFloors = Config.NUMBER_OF_FLOORS;
    private final List<Floor> floors = new CopyOnWriteArrayList<>();

    private Building() {
        for (int i = 0; i < numOfFloors; i++) {
            floors.add(new Floor(i + 1));
        }
    }

    public static Building getBuilding() {
        return building;
    }

    public List<Floor> getFloors() {
        return floors;
    }
}

package com.project.burger.controller;

import com.project.burger.model.Burger;

public class ConveyorController {
    private int currentStationIndex;
    private Burger burger;

    public void moveToNextStation() {
        if (currentStationIndex < 5) {
            currentStationIndex++;
            if (currentStationIndex == 4 && !hasFries()) {
                currentStationIndex = 5;
            }
        }
    }

    public void moveToPreviousStation() {
        if (currentStationIndex > 0) {
            currentStationIndex--;
            if (currentStationIndex == 4 && !hasFries()) {
                currentStationIndex = 3;
            }
        }
    }

    public int getCurrentStationIndex() {
        return currentStationIndex;
    }

    public void setBurger(Burger burger) {
        this.burger = burger;
    }

    public void reset() {
        currentStationIndex = 0;
    }

    private boolean hasFries() {
        return burger != null && burger.isFriesPresent();
    }
}

package com.project.burger.controller;

import com.project.burger.model.Burger;

public class ConveyorController {
    private int currentStationIndex;
    private Burger burger;

    public void moveToNextStation() {
        if (currentStationIndex < 5) {
            currentStationIndex++;
        }
    }

    public void moveToPreviousStation() {
        if (currentStationIndex > 0) {
            currentStationIndex--;
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

    private boolean hasNoFries() {
        return burger == null || !burger.isFriesPresent();
    }
}

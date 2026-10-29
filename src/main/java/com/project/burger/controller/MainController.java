package com.project.burger.controller;

import com.project.burger.engine.GameEngine;
import com.project.burger.model.Burger;

public class MainController {
    private final GameEngine gameEngine;
    private final ConveyorController conveyorController;
    private final StationController stationController;
    private final Burger currentBurger;
    private boolean orderCompletedAtDrinks;

    public MainController() {
        gameEngine = new GameEngine();
        conveyorController = new ConveyorController();
        currentBurger = new Burger();
        stationController = new StationController(currentBurger, gameEngine);
    }

    public void moveConveyorNext() {
        conveyorController.moveToNextStation();
        if (conveyorController.getCurrentStationIndex() == 6 && !orderCompletedAtDrinks) {
            stationController.deliverBurger();
            orderCompletedAtDrinks = true;
        }
    }

    public void moveConveyorPrevious() {
        conveyorController.moveToPreviousStation();
    }

    public void handleStartGame() {
        currentBurger.clear();
        gameEngine.startGame();
        conveyorController.reset();
        orderCompletedAtDrinks = false;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public ConveyorController getConveyorController() {
        return conveyorController;
    }

    public StationController getStationController() {
        return stationController;
    }

    public Burger getCurrentBurger() {
        return currentBurger;
    }
}

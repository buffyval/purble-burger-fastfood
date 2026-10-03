package com.project.burger.controller;

import com.project.burger.engine.GameEngine;
import com.project.burger.model.Burger;

public class MainController {
    private final GameEngine gameEngine;
    private final ConveyorController conveyorController;
    private final StationController stationController;
    private final Burger currentBurger;

    public MainController() {
        gameEngine = new GameEngine();
        conveyorController = new ConveyorController();
        currentBurger = new Burger();
        conveyorController.setBurger(currentBurger);
        stationController = new StationController(currentBurger, gameEngine);
    }

    public void moveConveyorNext() {
        conveyorController.moveToNextStation();
    }

    public void moveConveyorPrevious() {
        conveyorController.moveToPreviousStation();
    }

    public void handleStartGame() {
        currentBurger.clear();
        gameEngine.startGame();
        conveyorController.reset();
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

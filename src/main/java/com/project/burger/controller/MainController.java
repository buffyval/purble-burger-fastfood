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
            stationController.dispenseDrink();
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

    /*
    * public void handleDeliverBurger(BurgerView burgerView, Runnable refreshUI) {
        // 1. Invia il panino al GameEngine per calcolare il punteggio
        gameEngine.submitBurger(currentBurger);

        // 2. Avvia l'animazione del pacchetto per 2 secondi
        burgerView.showDeliveryPackage(() -> {
            // Questa callback viene eseguita DOPO i 2 secondi:
            if (gameEngine.getState() == GameState.PLAYING) {
                this.currentBurger = new Burger();
                conveyorController.reset(); // Riporta l'indice del nastro alla Stazione 0
            }
            // Aggiorna la schermata (Order #X incrementato e vassoio pulito)
            refreshUI.run();
        });
    }
    * */

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

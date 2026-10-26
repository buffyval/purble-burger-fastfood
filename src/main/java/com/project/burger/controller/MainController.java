package com.project.burger.controller;

import com.project.burger.engine.GameEngine;
import com.project.burger.engine.GameState;
import com.project.burger.model.Burger;
import com.project.burger.view.BurgerView;

public class MainController {
    private final GameEngine gameEngine;
    private final ConveyorController conveyorController;
    private final StationController stationController;
    private Burger currentBurger;
    private boolean orderCompletedAtDrinks;

    public MainController() {
        gameEngine = new GameEngine();
        conveyorController = new ConveyorController();
        currentBurger = new Burger();
        conveyorController.setBurger(currentBurger);
        stationController = new StationController(currentBurger, gameEngine);
    }

    public void moveConveyorNext() {
        conveyorController.moveToNextStation();
        if (conveyorController.getCurrentStationIndex() == 6 && !orderCompletedAtDrinks) {
            stationController.dispenseDrink();
            stationController.triggerMechanicArm();
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

    public void handleDeliverBurger(BurgerView burgerView, Runnable refreshUI) {
        // 1. Invio del panino corrente per la registrazione dell'ordine e del punteggio
        gameEngine.submitBurger(currentBurger);

        // 2. Avvio dell'animazione di copertura di 2 secondi
        burgerView.showDeliveryPackage(() -> {
            // Questa callback viene eseguita DOPO i 2 secondi:
            if (gameEngine.getState() == GameState.PLAYING) {
                this.currentBurger = new Burger();
                conveyorController.reset(); // Riporta l'indice del nastro a 0 (prima stazione)
            }
            // Aggiorna la schermata (Order #X incrementato, nuovi target e vassoio pulito)
            refreshUI.run();
        });
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    @SuppressWarnings("unused")
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

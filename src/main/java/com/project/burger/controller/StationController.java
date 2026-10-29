package com.project.burger.controller;

import com.project.burger.engine.GameEngine;
import com.project.burger.model.BreadShape;
import com.project.burger.model.Burger;
import com.project.burger.model.Ingredient;
import com.project.burger.model.IngredientType;

public class StationController {
    private final Burger burger;
    private final GameEngine gameEngine;

    public StationController(Burger burger, GameEngine gameEngine) {
        this.burger = burger;
        this.gameEngine = gameEngine;
    }

    public void applyShape(BreadShape shape) {
        burger.setShape(shape);
    }

    public void addAddition(IngredientType ingredientType) { addIngredient(ingredientType); }

    public void addFries() {
        burger.setFriesPresent(true);
    }

    public void applySauce(IngredientType sauceType) {
        burger.addSauce(sauceType);
    }

    public void deliverBurger() {
        if (gameEngine != null && burger != null) {
            gameEngine.submitBurger(burger);
        }
        resetBurger();
    }

    private void addIngredient(IngredientType ingredientType) {
        burger.addIngredient(new Ingredient(ingredientType, ingredientType.name(), ""));
    }

    public void resetBurger() { this.burger.clear(); }

    public void restartGame() {
        if (gameEngine != null) {
            gameEngine.startGame();
        }
        resetBurger();
    }
}

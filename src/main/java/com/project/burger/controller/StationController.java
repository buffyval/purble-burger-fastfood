package com.project.burger.controller;

import com.project.burger.engine.GameEngine;
import com.project.burger.model.BreadShape;
import com.project.burger.model.Burger;
import com.project.burger.model.Ingredient;
import com.project.burger.model.IngredientType;

public class StationController {
    private Burger burger;
    private final GameEngine gameEngine;

    public StationController(Burger burger, GameEngine gameEngine) {
        this.burger = burger;
        this.gameEngine = gameEngine;
    }

    public void applyShape(BreadShape shape) {
        burger.setShape(shape);
    }

    public void addMeat() {
        addIngredient(IngredientType.BEEF_PATTY);
    }

    public void addAddition(IngredientType ingredientType) {
        addIngredient(ingredientType);
    }

    public void addFries() {
        burger.setFriesPresent(true);
    }

    public void applySauce(IngredientType sauceType) {
        burger.setSauce(sauceType);
    }

    public void triggerMechanicArm(Burger burger) {
        gameEngine.submitBurger(burger);
    }

    public void triggerMechanicArm() {
        triggerMechanicArm(burger);
    }

    private void addIngredient(IngredientType ingredientType) {
        burger.addIngredient(new Ingredient(ingredientType, ingredientType.name(), ""));
    }
}

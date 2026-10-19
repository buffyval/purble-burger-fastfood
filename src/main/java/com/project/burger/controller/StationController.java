package com.project.burger.controller;

import com.project.burger.engine.GameEngine;
import com.project.burger.model.BreadShape;
import com.project.burger.model.Burger;
import com.project.burger.model.Ingredient;
import com.project.burger.model.IngredientType;
import com.project.burger.model.DrinkType;
import com.project.burger.model.Order;

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
        if (burger.getShape() == BreadShape.BAGUETTE) {
            // La baguette prende sempre il prosciutto
            addIngredient(IngredientType.HAM);
        } else if (burger.getShape() == BreadShape.CLASSIC) {
            // Il classic prende sempre la carne
            addIngredient(IngredientType.PATTY);
        } else if (burger.getShape() == BreadShape.BAGEL) {
            // Il bagel prende quello che l'ordine richiede
            Order currentOrder = gameEngine.getCurrentOrder();
            if (currentOrder != null && currentOrder.getTargetIngredients().contains(IngredientType.HAM)) {
                addIngredient(IngredientType.HAM);
            } else {
                addIngredient(IngredientType.PATTY);
            }
        }
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

    public void dispenseDrink() {
        DrinkType targetDrink = gameEngine.getCurrentOrder().getTargetDrink();
        if (targetDrink != null) {
            burger.setDrink(targetDrink);
        }
    }

    private void addIngredient(IngredientType ingredientType) {
        burger.addIngredient(new Ingredient(ingredientType, ingredientType.name(), ""));
    }
}

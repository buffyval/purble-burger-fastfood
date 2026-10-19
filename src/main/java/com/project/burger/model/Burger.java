package com.project.burger.model;

import java.util.ArrayList;
import java.util.List;

public class Burger {
    private BreadShape shape;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private boolean friesPresent;
    private IngredientType sauce;
    private DrinkType drink;

    public Burger() {
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void setShape(BreadShape shape) {
        this.shape = shape;
    }

    public void setFriesPresent(boolean friesPresent) {
        this.friesPresent = friesPresent;
    }

    public void setSauce(IngredientType sauce) {
        this.sauce = sauce;
    }

    public void clear() {
        shape = null;
        ingredients.clear();
        friesPresent = false;
        sauce = null;
        drink = null;
    }

    public BreadShape getShape() {
        return shape;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public boolean isFriesPresent() {
        return friesPresent;
    }

    public IngredientType getSauce() {
        return sauce;
    }

    public void setDrink(DrinkType drink) {
        this.drink = drink;
    }

    public DrinkType getDrink() {
        return drink;
    }
}

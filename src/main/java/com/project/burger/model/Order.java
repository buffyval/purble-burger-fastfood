package com.project.burger.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Order {
    private final BreadShape targetShape;
    private final List<IngredientType> targetIngredients;
    private final boolean targetFries;
    private final IngredientType targetSauce;

    public Order(BreadShape targetShape, List<IngredientType> targetIngredients,
                 boolean targetFries, IngredientType targetSauce) {
        this.targetShape = targetShape;
        this.targetIngredients = new ArrayList<>(targetIngredients);
        this.targetFries = targetFries;
        this.targetSauce = targetSauce;
    }

    public BreadShape getTargetShape() {
        return targetShape;
    }

    public List<IngredientType> getTargetIngredients() {
        return targetIngredients;
    }

    public boolean isTargetFries() {
        return targetFries;
    }

    public IngredientType getTargetSauce() {
        return targetSauce;
    }

    public boolean matches(Burger burger) {
        List<IngredientType> burgerIngredients = burger.getIngredients().stream()
                .map(Ingredient::getType)
                .collect(Collectors.toList());

        return Objects.equals(targetShape, burger.getShape())
                && targetIngredients.equals(burgerIngredients)
                && targetFries == burger.isFriesPresent()
                && Objects.equals(targetSauce, burger.getSauce());
    }
}

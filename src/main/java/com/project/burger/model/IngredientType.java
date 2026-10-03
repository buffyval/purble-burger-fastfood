package com.project.burger.model;

public enum IngredientType {
    BEEF_PATTY("MEAT"),
    LETTUCE("ADDITION"),
    CHEESE("ADDITION"),
    ONION("ADDITION"),
    PICKLES("ADDITION"),
    TOMATO("ADDITION"),
    FRIES("SIDE"),
    KETCHUP("SAUCE"),
    MUSTARD("SAUCE"),
    MAYONNAISE("SAUCE");

    private final String category;

    IngredientType(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}

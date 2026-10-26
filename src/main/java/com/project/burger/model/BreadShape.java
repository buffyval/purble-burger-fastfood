package com.project.burger.model;

public enum BreadShape {
    CLASSIC("Classic"),
    BAGUETTE("Baguette"),
    BAGEL("Bagel");

    private final String displayName;

    BreadShape(String displayName) {
        this.displayName = displayName;
    }

    @SuppressWarnings("unused") // potrebbe essere utile in futuro
    public String getDisplayName() {
        return displayName;
    }
}

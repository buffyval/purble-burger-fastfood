package com.project.burger.model;

public enum BreadShape {
    CLASSIC("Classic"),
    BAGUETTE("Baguette"),
    BAGEL("Bagel");

    private final String displayName;

    BreadShape(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

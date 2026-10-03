package com.project.burger.model;

public class Ingredient {
    private final IngredientType type;
    private final String name;
    private final String imagePath;

    public Ingredient(IngredientType type, String name, String imagePath) {
        this.type = type;
        this.name = name;
        this.imagePath = imagePath;
    }

    public IngredientType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}

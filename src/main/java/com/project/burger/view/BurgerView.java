package com.project.burger.view;

import com.project.burger.model.BreadShape;
import com.project.burger.model.Burger;
import com.project.burger.model.Ingredient;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;

public class BurgerView extends VBox {
    private static final double STANDARD_WIDTH = 140;
    private static final double BAGUETTE_WIDTH = 260;
    private static final double INGREDIENT_HEIGHT = 35;

    public BurgerView() {
        setAlignment(Pos.CENTER);
        setSpacing(-5); // Sovrapposizione leggera verticale per evitare l'effetto "esploso"
    }

    public void renderBurger(Burger burger) {
        getChildren().clear();

        if (burger == null) {
            return;
        }

        BreadShape shape = burger.getShape() == null ? BreadShape.CLASSIC : burger.getShape();
        boolean isBaguette = (shape == BreadShape.BAGUETTE);

        // 1. Pane inferiore
        getChildren().add(createImageView(
                "/assets/bun_" + shape.name().toLowerCase() + "_bottom.png",
                isBaguette ? BAGUETTE_WIDTH : STANDARD_WIDTH, 45));

        // 2. Ingredienti
        for (Ingredient ingredient : burger.getIngredients()) {
            String path = ingredientImagePath(ingredient.getType());
            getChildren().add(createIngredientNode(path, isBaguette, INGREDIENT_HEIGHT));
        }

        // 3. Salsa
        if (burger.getSauce() != null) {
            String saucePath = ingredientImagePath(burger.getSauce());
            getChildren().add(createIngredientNode(saucePath, isBaguette, 20));
        }

        // 4. Pane superiore
        getChildren().add(createImageView(
                "/assets/bun_" + shape.name().toLowerCase() + "_top.png",
                isBaguette ? BAGUETTE_WIDTH : STANDARD_WIDTH, 45));

        // 5. Patatine opzionali
        if (burger.isFriesPresent()) {
            getChildren().add(createImageView("/assets/fries.png", STANDARD_WIDTH, 45));
        }
    }

    /**
     * Crea un singolo ingrediente centrato per qualsiasi forma di pane.
     */
    private Node createIngredientNode(String resourcePath, boolean isBaguette, double height) {
        return createImageView(
                resourcePath,
                isBaguette ? BAGUETTE_WIDTH : STANDARD_WIDTH,
                height);
    }

    private String ingredientImagePath(com.project.burger.model.IngredientType ingredient) {
        if (ingredient == com.project.burger.model.IngredientType.HAM) {
            return "/assets/ingredient_ham.png";
        }
        return "/assets/ingredient_" + ingredient.name().toLowerCase() + ".png";
    }

    private ImageView createImageView(String resourcePath, double fitWidth, double fitHeight) {
        ImageView imageView = new ImageView();
        URL imageUrl = getClass().getResource(resourcePath);
        if (imageUrl != null) {
            imageView.setImage(new Image(imageUrl.toExternalForm()));
        }
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        imageView.setPreserveRatio(true);
        return imageView;
    }
}
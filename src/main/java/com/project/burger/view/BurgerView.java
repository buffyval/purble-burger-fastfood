package com.project.burger.view;

import com.project.burger.model.BreadShape;
import com.project.burger.model.Burger;
import com.project.burger.model.Ingredient;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;

public class BurgerView extends VBox {
    private static final double STANDARD_WIDTH = 140;
    private static final double BAGUETTE_WIDTH = 260;
    private static final double SINGLE_PORTION_WIDTH = 125; // Larghezza della singola fetta duplicata
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
                "/assets/bottom_bun_" + shape.name().toLowerCase() + ".png",
                isBaguette ? BAGUETTE_WIDTH : STANDARD_WIDTH, 45));

        // 2. Ingredienti (Duplicati affiancati se Baguette)
        for (Ingredient ingredient : burger.getIngredients()) {
            String path = "/assets/ingredient_" + ingredient.getType().name().toLowerCase() + ".png";
            getChildren().add(createIngredientNode(path, isBaguette, INGREDIENT_HEIGHT));
        }

        // 3. Salsa (Duplicata affiancata se Baguette)
        if (burger.getSauce() != null) {
            String saucePath = "/assets/ingredient_" + burger.getSauce().name().toLowerCase() + ".png";
            getChildren().add(createIngredientNode(saucePath, isBaguette, 20));
        }

        // 4. Pane superiore
        getChildren().add(createImageView(
                "/assets/top_bun_" + shape.name().toLowerCase() + ".png",
                isBaguette ? BAGUETTE_WIDTH : STANDARD_WIDTH, 45));

        // 5. Patatine opzionali
        if (burger.isFriesPresent()) {
            getChildren().add(createImageView("/assets/fries.png", STANDARD_WIDTH, 45));
        }
    }

    /**
     * Crea un Node singolo (ImageView) se tondo, o un HBox con due copie affiancate se Baguette.
     */
    private Node createIngredientNode(String resourcePath, boolean isBaguette, double height) {
        if (isBaguette) {
            HBox doubleIngredientBox = new HBox(2); // 2px di distanziamento orizzontale
            doubleIngredientBox.setAlignment(Pos.CENTER);

            ImageView leftImage = createImageView(resourcePath, SINGLE_PORTION_WIDTH, height);
            ImageView rightImage = createImageView(resourcePath, SINGLE_PORTION_WIDTH, height);

            doubleIngredientBox.getChildren().addAll(leftImage, rightImage);
            return doubleIngredientBox;
        } else {
            return createImageView(resourcePath, STANDARD_WIDTH, height);
        }
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
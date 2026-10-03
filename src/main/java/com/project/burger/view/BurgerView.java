package com.project.burger.view;

import com.project.burger.model.BreadShape;
import com.project.burger.model.Burger;
import com.project.burger.model.Ingredient;
import javafx.geometry.Pos;
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
    }

    public void renderBurger(Burger burger) {
        getChildren().clear();

        if (burger == null) {
            return;
        }

        BreadShape shape = burger.getShape() == null ? BreadShape.CLASSIC : burger.getShape();
        boolean baguette = shape == BreadShape.BAGUETTE;

        getChildren().add(createImageView(
                "/images/bottom_bun_" + shape.name().toLowerCase() + ".png",
                baguette ? BAGUETTE_WIDTH : STANDARD_WIDTH,
                45));

        for (Ingredient ingredient : burger.getIngredients()) {
            ImageView ingredientView = createImageView(
                    "/images/ingredient_" + ingredient.getType().name().toLowerCase() + ".png",
                    baguette ? BAGUETTE_WIDTH : STANDARD_WIDTH,
                    INGREDIENT_HEIGHT);
            getChildren().add(ingredientView);
        }

        if (burger.getSauce() != null) {
            getChildren().add(createImageView(
                    "/images/sauce_" + burger.getSauce().name().toLowerCase() + ".png",
                    baguette ? BAGUETTE_WIDTH : STANDARD_WIDTH,
                    20));
        }

        getChildren().add(createImageView(
                "/images/top_bun_" + shape.name().toLowerCase() + ".png",
                baguette ? BAGUETTE_WIDTH : STANDARD_WIDTH,
                45));

        if (burger.isFriesPresent()) {
            getChildren().add(createImageView("/images/fries.png", STANDARD_WIDTH, 45));
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

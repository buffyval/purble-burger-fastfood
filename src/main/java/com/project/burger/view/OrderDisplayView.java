package com.project.burger.view;

import com.project.burger.model.BreadShape;
import com.project.burger.model.IngredientType;
import com.project.burger.model.Order;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;

public class OrderDisplayView extends BorderPane {
    private static final double ORDER_WIDTH = 160;
    private static final double INGREDIENT_HEIGHT = 30;
    private static final int STAR_COUNT = 5;

    private final VBox orderBox = new VBox(0);
    private final HBox starsBox = new HBox(3);
    private final Label[] stars = new Label[STAR_COUNT];

    public OrderDisplayView() {
        orderBox.setAlignment(Pos.TOP_LEFT);
        orderBox.setStyle("-fx-padding: 10;");

        starsBox.setAlignment(Pos.TOP_RIGHT);
        starsBox.setStyle("-fx-padding: 10;");
        for (int index = 0; index < STAR_COUNT; index++) {
            stars[index] = new Label();
            starsBox.getChildren().add(stars[index]);
        }

        setLeft(orderBox);
        setRight(starsBox);
        starsBox.setVisible(false); // modifica 01
    }

    public void renderOrder(Order order, int currentBurgerCount, int scoreStars) {
        orderBox.getChildren().clear();

        starsBox.setVisible(false); // modifica 02

        BreadShape shape = order == null || order.getTargetShape() == null
                ? BreadShape.CLASSIC
                : order.getTargetShape();
        orderBox.getChildren().add(createImageView(
                "/assets/top_bun_" + shape.name().toLowerCase() + ".png", 45));

        if (order != null) {
            if (order.getTargetSauce() != null) {
                addIngredientImage(order.getTargetSauce());
            }

            order.getTargetIngredients().stream()
                    .filter(ingredient -> ingredient != IngredientType.PATTY)
                    .forEach(this::addIngredientImage);
        }

        addIngredientImage(IngredientType.PATTY);
        orderBox.getChildren().add(createImageView(
                "/assets/bottom_bun_" + shape.name().toLowerCase() + ".png", 45));
        renderStars(scoreStars);
    }

    private void addIngredientImage(IngredientType ingredient) {
        String imagePath = "/assets/ingredient_"
                + ingredient.name().toLowerCase() + ".png";
        orderBox.getChildren().add(createImageView(imagePath, INGREDIENT_HEIGHT));
    }

    private void renderStars(int score) {
        int filledStars = clamp(score);
        for (int index = 0; index < stars.length; index++) {
            stars[index].setText(index < filledStars ? "★" : "☆");
            stars[index].setStyle(index < filledStars
                    ? "-fx-font-size: 24px; -fx-text-fill: #f2b632;"
                    : "-fx-font-size: 24px; -fx-text-fill: #d6b36a;");
        }
    }

    private ImageView createImageView(String resourcePath, double height) {
        ImageView imageView = new ImageView();
        URL imageUrl = getClass().getResource(resourcePath);
        if (imageUrl != null) {
            imageView.setImage(new Image(imageUrl.toExternalForm()));
        }
        imageView.setFitWidth(ORDER_WIDTH);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(STAR_COUNT, value));
    }
}

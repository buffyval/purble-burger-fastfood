package com.project.burger.view;

import com.project.burger.model.BreadShape;
import com.project.burger.model.IngredientType;
import com.project.burger.model.Order;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;

public class OrderDisplayView extends BorderPane {

    // PARAMETRI DEL PANINO (Modificabili a piacimento)
    private static final double STANDARD_WIDTH = 200;
    private static final double BUN_HEIGHT = 100;
    private static final double INGREDIENT_HEIGHT = 100;
    private static final double BAGUETTE_PIECE_WIDTH = 90;
    private static final int STAR_COUNT = 5;

    // Spacing fortemente negativo per unire visivamente le fette
    private final VBox orderBox = new VBox(-84);
    private final Label[] stars = new Label[STAR_COUNT];

    public OrderDisplayView() {
        setPadding(new Insets(25, 0, 0, 30));
        orderBox.setAlignment(Pos.CENTER);

        // Fissa la dimensione massima del riquadro dell'ordine
        orderBox.setMaxSize(240, 320);
        orderBox.setMinSize(240, 320);

        StackPane displayContainer = new StackPane();
        displayContainer.setMaxSize(240, 320);

        URL displayBgUrl = getClass().getResource("/assets/order_display_frame.png");
        if (displayBgUrl != null) {
            ImageView frameView = createImageView("/assets/order_display_frame.png", 240, 320, false);
            displayContainer.getChildren().addAll(frameView, orderBox);
        } else {
            orderBox.setStyle("-fx-background-color: rgba(255, 248, 231, 0.95); " +
                    "-fx-border-color: #d6b36a; -fx-border-width: 3px; " +
                    "-fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 10;");
            displayContainer.getChildren().add(orderBox);
        }

        HBox starsBox = new HBox(3);
        starsBox.setAlignment(Pos.TOP_RIGHT);
        starsBox.setStyle("-fx-padding: 10;");
        for (int index = 0; index < STAR_COUNT; index++) {
            stars[index] = new Label();
            stars[index].setStyle("-fx-font-size: 24px;");
            starsBox.getChildren().add(stars[index]);
        }

        setLeft(displayContainer);
        setRight(starsBox);
        starsBox.setVisible(false);
    }

    public void renderOrder(Order order, @SuppressWarnings("unused") int currentBurgerCount, int scoreStars) {
        orderBox.getChildren().clear();

        BreadShape shape = (order == null || order.getTargetShape() == null)
                ? BreadShape.CLASSIC
                : order.getTargetShape();
        boolean isBaguette = (shape == BreadShape.BAGUETTE);

        double bunWidth = isBaguette ? STANDARD_WIDTH + 40 : STANDARD_WIDTH;

        // 1. Inserisci prima il TOP BUN -> va IN CIMA alla VBox
        orderBox.getChildren().add(createImageView(
                "/assets/top_bun_" + shape.name().toLowerCase() + ".png", bunWidth, BUN_HEIGHT, true));

        if (order != null) {
            // 2. Inserisci la SALSA
            if (order.getTargetSauce() != null) {
                addIngredientNode(order.getTargetSauce(), isBaguette);
            }

            // 3. Inserisci gli INGREDIENTI extra (esclusa la carne)
            order.getTargetIngredients().stream()
                    .filter(ingredient -> ingredient != IngredientType.PATTY)
                    .forEach(ingredient -> addIngredientNode(ingredient, isBaguette));
        }

        // 4. Inserisci la CARNE (PATTY)
        addIngredientNode(IngredientType.PATTY, isBaguette);

        // 5. Inserisci per ultimo il BOTTOM BUN -> va ALLA BASE della VBox
        orderBox.getChildren().add(createImageView(
                "/assets/bottom_bun_" + shape.name().toLowerCase() + ".png", bunWidth, BUN_HEIGHT, true));

        renderStars(scoreStars);
    }

    private void addIngredientNode(IngredientType ingredient, boolean isBaguette) {
        String imagePath = "/assets/ingredient_" + ingredient.name().toLowerCase() + ".png";

        if (isBaguette) {
            HBox doubleBox = new HBox(-12);
            doubleBox.setAlignment(Pos.CENTER);

            ImageView img1 = createImageView(imagePath, BAGUETTE_PIECE_WIDTH, INGREDIENT_HEIGHT, true);
            ImageView img2 = createImageView(imagePath, BAGUETTE_PIECE_WIDTH, INGREDIENT_HEIGHT, true);

            doubleBox.getChildren().addAll(img1, img2);
            orderBox.getChildren().add(doubleBox);
        } else {
            orderBox.getChildren().add(createImageView(imagePath, STANDARD_WIDTH, INGREDIENT_HEIGHT, true));
        }
    }

    private void renderStars(int score) {
        int filledStars = clamp(score);
        for (int index = 0; index < stars.length; index++) {
            boolean isFilled = index < filledStars;
            stars[index].setText(isFilled ? "★" : "☆");
            stars[index].setTextFill(isFilled ? Color.web("#f2b632") : Color.web("#d6b36a"));
        }
    }

    private ImageView createImageView(String resourcePath, double width, double height, boolean preserveRatio) {
        ImageView imageView = new ImageView();
        URL imageUrl = getClass().getResource(resourcePath);
        if (imageUrl != null) {
            imageView.setImage(new Image(imageUrl.toExternalForm()));
        }
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(preserveRatio);
        return imageView;
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(STAR_COUNT, value));
    }
}
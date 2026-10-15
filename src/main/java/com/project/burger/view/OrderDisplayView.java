package com.project.burger.view;

import com.project.burger.model.BreadShape;
import com.project.burger.model.IngredientType;
import com.project.burger.model.Order;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OrderDisplayView extends BorderPane {

    // PARAMETRI REGOLABILI PER LA DIMENSIONE GENERALE
    private static final double STANDARD_WIDTH = 130;
    private static final double BUN_HEIGHT = 100;
    private static final double INGREDIENT_HEIGHT = 100;
    private static final double BAGUETTE_PIECE_WIDTH = 130;
    private static final int STAR_COUNT = 5;

    // Spacing verticale base della VBox
    private final VBox orderBox = new VBox(-80);
    private final Label[] stars = new Label[STAR_COUNT];

    public OrderDisplayView() {
        setPadding(new Insets(20, 0, 0, 25));
        orderBox.setAlignment(Pos.CENTER);

        // Dimensioni fisse del contenitore per isolarlo
        orderBox.setMaxSize(220, 240);
        orderBox.setMinSize(220, 240);

        StackPane displayContainer = new StackPane();
        displayContainer.setMaxSize(220, 240);

        URL displayBgUrl = getClass().getResource("/assets/order_display_frame.png");
        if (displayBgUrl != null) {
            ImageView frameView = createImageView("/assets/order_display_frame.png", 220, 240);
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
        double bunWidth = isBaguette ? STANDARD_WIDTH + 30 : STANDARD_WIDTH;

        // 1. TOP BUN (In cima)
        ImageView topBunView = createImageView(
                "/assets/bun_" + shape.name().toLowerCase() + "_top.png", bunWidth, BUN_HEIGHT);
        orderBox.getChildren().add(topBunView);

        // Lista dinamica degli ingredienti EFFETTIVAMENTE presenti
        List<IngredientType> presentIngredients = new ArrayList<>();

        if (order != null) {
            // Ordine canonico di inserimento dall'alto verso il basso
            IngredientType[] canonicalOrder = {
                    IngredientType.LETTUCE,
                    IngredientType.ONION,
                    IngredientType.TOMATO,
                    IngredientType.PICKLES,
                    IngredientType.CHEESE
            };

            for (IngredientType type : canonicalOrder) {
                if (order.getTargetIngredients().contains(type)) {
                    presentIngredients.add(type);
                }
            }
        }

        // 2. Aggiungi gli ingredienti effettivamente presenti alla VBox
        for (IngredientType ingredient : presentIngredients) {
            addIngredientNode(ingredient, isBaguette);
        }

        // 3. PATTY (Carne)
        addIngredientNode(IngredientType.PATTY, isBaguette);

        // 4. BOTTOM BUN (Base)
        orderBox.getChildren().add(createImageView(
                "/assets/bun_" + shape.name().toLowerCase() + "_bottom.png", bunWidth, BUN_HEIGHT));

        // 5. REGOLAZIONE DINAMICA DELLA SOVRAPPOSIZIONE E DEGLI OFFSET
        applyDynamicLayout(shape);

        renderStars(scoreStars);
    }

    private void addIngredientNode(IngredientType ingredient, boolean isBaguette) {
        String imagePath = "/assets/ingredient_" + ingredient.name().toLowerCase() + ".png";

        if (isBaguette) {
            HBox doubleBox = new HBox(-5);
            doubleBox.setAlignment(Pos.CENTER);
            ImageView img1 = createImageView(imagePath, BAGUETTE_PIECE_WIDTH, INGREDIENT_HEIGHT);
            ImageView img2 = createImageView(imagePath, BAGUETTE_PIECE_WIDTH, INGREDIENT_HEIGHT);
            doubleBox.getChildren().addAll(img1, img2);
            doubleBox.setUserData(ingredient); // Salviamo il tipo di ingrediente per riferimento futuro
            orderBox.getChildren().add(doubleBox);
        } else {
            ImageView singleImg = createImageView(imagePath, STANDARD_WIDTH, INGREDIENT_HEIGHT);
            singleImg.setUserData(ingredient); // Salviamo il tipo di ingrediente
            orderBox.getChildren().add(singleImg);
        }
    }

    /**
     * Calcola gli offset visivi e l'ordine Z dinamico basandosi ESCLUSIVAMENTE
     * sugli strati realmente presenti nella VBox, evitando disallineamenti se mancano ingredienti.
     */
    private void applyDynamicLayout(BreadShape shape) {
        int totalElements = orderBox.getChildren().size();
        boolean isBagel = (shape == BreadShape.BAGEL);

        for (int i = 0; i < totalElements; i++) {
            Node node = orderBox.getChildren().get(i);

            // Assicura l'ordine Z corretto (Top Bun davanti)
            node.setViewOrder(i);

            // Calcolo offset in base all'ingrediente effettivo
            IngredientType type = (node.getUserData() instanceof IngredientType)
                    ? (IngredientType) node.getUserData()
                    : null;

            if (i == 0) { // Top Bun
                node.setTranslateY(isBagel ? 18 : 15);
            } else if (type != null) {
                // Compensazione graduale calcolata sull'indice reale nell'array dei nodi presenti
                int layerOffset = i * 2;
                int ingredientSpecificOffset = getIngredientBaseOffset(type, isBagel);

                node.setTranslateY(ingredientSpecificOffset + layerOffset);
            } else if (i == totalElements - 1) { // Bottom Bun
                node.setTranslateY(0);
            }
        }
    }

    private int getIngredientBaseOffset(IngredientType ingredient, boolean isBagel) {
        if (isBagel) {
            return switch (ingredient) {
                case PATTY -> 8;
                case CHEESE -> 14;
                case PICKLES -> 18;
                case TOMATO -> 10;
                case ONION -> 14;
                case LETTUCE -> 12;
                default -> 0;
            };
        } else {
            return switch (ingredient) {
                case PATTY -> 6;
                case CHEESE -> 12;
                case PICKLES -> 16;
                case TOMATO -> 8;
                case ONION -> 12;
                case LETTUCE -> 10;
                default -> 0;
            };
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

    private ImageView createImageView(String resourcePath, double width, double height) {
        ImageView imageView = new ImageView();
        URL imageUrl = getClass().getResource(resourcePath);
        if (imageUrl != null) {
            imageView.setImage(new Image(imageUrl.toExternalForm()));
        }
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(false);
        return imageView;
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(STAR_COUNT, value));
    }
}
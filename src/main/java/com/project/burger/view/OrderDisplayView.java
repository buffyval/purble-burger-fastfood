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

    // =========================================================
    // DIMENSIONI IMMAGINI
    // =========================================================

    private static final double STANDARD_WIDTH = 100;
    private static final double BUN_HEIGHT = 100;
    private static final double INGREDIENT_HEIGHT = 100;

    private static final double BAGUETTE_WIDTH = 160;

    private static final int STAR_COUNT = 5;

    // =========================================================
    // DIMENSIONI DISPLAY
    // =========================================================

    private static final double DISPLAY_WIDTH = 220;
    private static final double DISPLAY_HEIGHT = 240;

    /*
     * StackPane anziché VBox.
     * In questo modo decido esattamente dove posizionare
     * ogni ingrediente.
     */
    private final StackPane orderBox = new StackPane();
    private final Label[] stars = new Label[STAR_COUNT];

    public OrderDisplayView() {

        setPadding(new Insets(20, 0, 0, 25));

        orderBox.setAlignment(Pos.CENTER);

        orderBox.setPrefSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        orderBox.setMinSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        orderBox.setMaxSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);

        StackPane displayContainer = new StackPane();

        displayContainer.setPrefSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        displayContainer.setMinSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        displayContainer.setMaxSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);


        URL displayBgUrl =
                getClass().getResource("/assets/order_display_frame.png");


        if (displayBgUrl != null) {

            ImageView frameView = createImageView(
                    "/assets/order_display_frame.png",
                    DISPLAY_WIDTH,
                    DISPLAY_HEIGHT
            );

            displayContainer.getChildren().addAll(
                    frameView,
                    orderBox
            );

        } else {

            orderBox.setStyle(
                    "-fx-background-color: rgba(255, 248, 231, 0.95);" +
                            "-fx-border-color: #d6b36a;" +
                            "-fx-border-width: 3px;" +
                            "-fx-border-radius: 12;" +
                            "-fx-background-radius: 12;"
            );

            displayContainer.getChildren().add(orderBox);
        }


        // =====================================================
        // STELLE
        // =====================================================

        HBox starsBox = new HBox(3);

        starsBox.setAlignment(Pos.TOP_RIGHT);
        starsBox.setStyle("-fx-padding: 10;");


        for (int i = 0; i < STAR_COUNT; i++) {

            stars[i] = new Label();

            stars[i].setStyle(
                    "-fx-font-size: 24px;"
            );

            starsBox.getChildren().add(stars[i]);
        }


        setLeft(displayContainer);
        setRight(starsBox);

        starsBox.setVisible(false);
    }


    // =========================================================
    // RENDER ORDINE
    // =========================================================

    public void renderOrder(
            Order order,
            @SuppressWarnings("unused") int currentBurgerCount,
            int scoreStars
    ) {

        orderBox.getChildren().clear();


        // =====================================================
        // FORMA
        // =====================================================

        BreadShape shape =
                (order == null || order.getTargetShape() == null)
                        ? BreadShape.CLASSIC
                        : order.getTargetShape();


        boolean isBaguette =
                shape == BreadShape.BAGUETTE;


        double bunWidth =
                isBaguette
                        ? BAGUETTE_WIDTH
                        : STANDARD_WIDTH;

        List<Node> layers = new ArrayList<>();

        // =====================================================
        // TOP BUN
        // =====================================================

        ImageView topBun = createImageView(
                "/assets/bun_"
                        + shape.name().toLowerCase()
                        + "_top.png",

                bunWidth,
                BUN_HEIGHT
        );

        topBun.setUserData(null);
        if (isBaguette) {
            topBun.setTranslateY(10); // valore per top bun baguette
        }

        layers.add(topBun);


        // =====================================================
        // INGREDIENTI PRESENTI
        // =====================================================

        if (order != null &&
                order.getTargetIngredients() != null) {

            IngredientType[] ingredientOrder = {

                    IngredientType.LETTUCE,
                    IngredientType.ONION,
                    IngredientType.TOMATO,
                    IngredientType.PICKLES,
                    IngredientType.CHEESE
            };


            for (IngredientType ingredient : ingredientOrder) {

                if (order
                        .getTargetIngredients()
                        .contains(ingredient)) {

                    layers.add(
                            createIngredientNode(
                                    ingredient,
                                    isBaguette
                            )
                    );
                }
            }
        }


        // =====================================================
        // PATTY or HAM
        // =====================================================

        IngredientType meatIngredient = IngredientType.PATTY;
        if (order != null && order.getTargetIngredients() != null) {
            if (order.getTargetIngredients().contains(IngredientType.HAM)) {
                meatIngredient = IngredientType.HAM;
            }
        }

        layers.add(
                createIngredientNode(
                        meatIngredient,
                        isBaguette
                )
        );

        // =====================================================
        // BOTTOM BUN
        // =====================================================

        ImageView bottomBun = createImageView(
                "/assets/bun_"
                        + shape.name().toLowerCase()
                        + "_bottom.png",

                bunWidth,
                BUN_HEIGHT
        );

        bottomBun.setUserData(null);

        layers.add(bottomBun);


        // =====================================================
        // POSIZIONAMENTO
        // =====================================================

        layoutLayers(layers, isBaguette);


        // =====================================================
        // STELLE
        // =====================================================

        renderStars(scoreStars);
    }


    // =========================================================
    // CREA INGREDIENTE
    // =========================================================

    private Node createIngredientNode(
            IngredientType ingredient,
            boolean isBaguette
    ) {

        String imagePath = ingredientImagePath(ingredient);

        ImageView ingredientView = createImageView(
                imagePath,
                isBaguette ? BAGUETTE_WIDTH : STANDARD_WIDTH,
                INGREDIENT_HEIGHT
        );

        ingredientView.setUserData(ingredient);

        return ingredientView;
    }

    // =========================================================
    // PERCORSO IMMAGINE INGREDIENTE PRINCIPALE
    // =========================================================

    private String ingredientImagePath(IngredientType ingredient) {
        if (ingredient == IngredientType.HAM) {
            return "/assets/ingredient_ham.png";
        }
        return "/assets/ingredient_" + ingredient.name().toLowerCase() + ".png";
    }


    // =========================================================
    // LAYOUT DINAMICO
    // =========================================================

    private void layoutLayers(List<Node> layers, boolean isBaguette) {

        double currentY = -55; // valore per spostare l'intero panino dentro il display

        for (int i = 0; i < layers.size(); i++) {

            Node node = layers.get(i);
            node.setTranslateY(currentY);
            /*
             * Gli elementi più in alto nel panino devono essere
             * disegnati davanti agli elementi sottostanti.
             */
            node.setViewOrder(i);
            // Aggiunge il nodo allo StackPane.
            orderBox.getChildren().add(node);


            /*
             * -------------------------------------------------
             * CALCOLO DELLA DISTANZA DAL PROSSIMO LAYER
             * -------------------------------------------------
             */

            currentY += getLayerStep(node, isBaguette);
        }
    }


    // =========================================================
    // DISTANZA SPECIFICA PER INGREDIENTE
    // =========================================================

    private double getLayerStep(Node node, boolean isBaguette) {

        Object data = node.getUserData();

        // Se non è un ingrediente, allora è un bun
        if (!(data instanceof IngredientType ingredient)) {
            return isBaguette ? 14 : 19; // distanziare il bottom bun dal top bun (differenza con baguette)
        }

        return switch (ingredient) {
            case LETTUCE, HAM -> 10;
            case CHEESE -> 4;
            case ONION -> 2;
            case TOMATO -> 12;
            case PICKLES -> 8;
            case PATTY -> 12.2;

            default -> 15.5;
        };
    }


    // =========================================================
    // STELLE
    // =========================================================

    private void renderStars(int score) {

        int filledStars = clamp(score);


        for (int i = 0; i < stars.length; i++) {

            boolean isFilled =
                    i < filledStars;


            stars[i].setText(
                    isFilled
                            ? "★"
                            : "☆"
            );


            stars[i].setTextFill(
                    isFilled
                            ? Color.web("#f2b632")
                            : Color.web("#d6b36a")
            );
        }
    }

    // =========================================================
    // IMAGE VIEW
    // =========================================================

    private ImageView createImageView(
            String resourcePath,
            double width,
            double height
    ) {

        ImageView imageView =
                new ImageView();


        URL imageUrl =
                getClass().getResource(resourcePath);


        if (imageUrl != null) {

            imageView.setImage(
                    new Image(
                            imageUrl.toExternalForm()
                    )
            );

        } else {

            System.err.println(
                    "Immagine non trovata: "
                            + resourcePath
            );
        }

        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);

        return imageView;
    }

    // =========================================================
    // CLAMP
    // =========================================================

    private int clamp(int value) {

        return Math.clamp(
                value
                ,
                0,
                STAR_COUNT);
    }
}
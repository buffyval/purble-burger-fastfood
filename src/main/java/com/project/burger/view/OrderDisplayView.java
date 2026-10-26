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
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
    private final Label orderNumberLabel = new Label();
    private final Label[] stars = new Label[STAR_COUNT];

    public OrderDisplayView() {
        setPadding(new Insets(20, 0, 0, 25));

        // =====================================================
        // STELLE
        // =====================================================
        HBox starsBox = new HBox(3);
        starsBox.setAlignment(Pos.TOP_LEFT); // Allineate a sinistra
        starsBox.setStyle("-fx-padding: 0 0 8 0;");

        IntStream.range(0, STAR_COUNT).forEach(i -> {
            stars[i] = new Label("☆"); // Stelle inizialmente vuote
            stars[i].setStyle("-fx-font-size: 24px;");
            stars[i].setTextFill(Color.web("#d6b36a"));
            starsBox.getChildren().add(stars[i]);
        });
        starsBox.setVisible(true);

        // =====================================================
        // DISPLAY SCHEDA ORDINE
        // =====================================================
        orderBox.setAlignment(Pos.CENTER);
        orderBox.setPrefSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        orderBox.setMinSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        orderBox.setMaxSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);

        StackPane displayContainer = new StackPane();
        displayContainer.setPrefSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        displayContainer.setMinSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        displayContainer.setMaxSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);

        // =====================================================
        // LABEL NUMERO ORDINE (In alto a sinistra del display)
        // =====================================================
        orderNumberLabel.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #4A3B32;"
        );
        StackPane.setAlignment(orderNumberLabel, Pos.TOP_LEFT);
        orderNumberLabel.setTranslateX(15);
        orderNumberLabel.setTranslateY(12);

        URL displayBgUrl = getClass().getResource("/assets/order_display_frame.png");

        if (displayBgUrl != null) {
            ImageView frameView = createImageView("/assets/order_display_frame.png", DISPLAY_WIDTH, DISPLAY_HEIGHT);
            displayContainer.getChildren().addAll(frameView, orderBox);
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

        // Raggruppa le stelle (sopra) e la scheda dell'ordine (sotto) a SINISTRA
        VBox leftContainer = new VBox(5, starsBox, displayContainer);
        setLeft(leftContainer);
    }

    // =========================================================
    // RENDER ORDINE
    // =========================================================
    public void renderOrder(Order order, int currentBurgerCount, int scoreStars) {
        orderBox.getChildren().clear();

        int orderNumber = Math.clamp(currentBurgerCount <= 0 ? 1 : currentBurgerCount, 1, 5);
        orderNumberLabel.setText("Order #" + orderNumber);
        orderBox.getChildren().add(orderNumberLabel);

        // =====================================================
        // FORMA
        // =====================================================
        BreadShape shape = Optional.ofNullable(order)
                .map(Order::getTargetShape)
                .orElse(BreadShape.CLASSIC);

        boolean isBaguette = shape == BreadShape.BAGUETTE;
        double bunWidth = isBaguette ? BAGUETTE_WIDTH : STANDARD_WIDTH;

        List<Node> layers = new ArrayList<>();

        // =====================================================
        // TOP BUN
        // =====================================================
        ImageView topBun = createImageView("/assets/bun_" + shape.name().toLowerCase() + "_top.png", bunWidth, BUN_HEIGHT);
        topBun.setUserData(null);
        if (isBaguette) {
            topBun.setTranslateY(10); // valore per top bun baguette
        }
        layers.add(topBun);

        // =====================================================
        // INGREDIENTI PRESENTI
        // =====================================================
        Optional.ofNullable(order)
                .map(Order::getTargetIngredients)
                .ifPresent(ingredients ->
                        Stream.of(
                                        IngredientType.LETTUCE,
                                        IngredientType.ONION,
                                        IngredientType.TOMATO,
                                        IngredientType.PICKLES,
                                        IngredientType.CHEESE
                                )
                                .filter(ingredients::contains)
                                .map(ingredient -> createIngredientNode(ingredient, isBaguette))
                                .forEach(layers::add)
                );

        // =====================================================
        // PATTY or HAM
        // =====================================================
        IngredientType meatIngredient = isBaguette
                ? IngredientType.HAM
                : Optional.ofNullable(order)
                .map(Order::getTargetIngredients)
                .filter(ingredients -> ingredients.contains(IngredientType.HAM))
                .map(ingredients -> IngredientType.HAM)
                .orElse(IngredientType.PATTY);

        layers.add(createIngredientNode(meatIngredient, isBaguette));

        // =====================================================
        // BOTTOM BUN
        // =====================================================
        ImageView bottomBun = createImageView("/assets/bun_" + shape.name().toLowerCase() + "_bottom.png", bunWidth, BUN_HEIGHT);
        bottomBun.setUserData(null);
        layers.add(bottomBun);

        // =====================================================
        // POSIZIONAMENTO LAYERS PANINO
        // =====================================================
        layoutLayers(layers, isBaguette);

        // =====================================================
        // TO-DO LIST OPZIONI
        // =====================================================
        boolean hasFries = Optional.ofNullable(order).map(Order::isTargetFries).orElse(false);
        boolean hasKetchup = Optional.ofNullable(order).map(Order::getTargetSauce).filter(s -> s == IngredientType.KETCHUP).isPresent();
        boolean hasMustard = Optional.ofNullable(order).map(Order::getTargetSauce).filter(s -> s == IngredientType.MUSTARD).isPresent();

        VBox todoBox = new VBox(2,
                createOptionLabel((hasFries ? "[✔] " : "[   ] ") + "Fries"),
                createOptionLabel((hasKetchup ? "[✔] " : "[   ] ") + "Ketchup"),
                createOptionLabel((hasMustard ? "[✔] " : "[   ] ") + "Mustard")
        );
        todoBox.setAlignment(Pos.CENTER_LEFT);
        todoBox.setTranslateY(55); // Sposta la to-do list sotto il bottom bun
        todoBox.setPadding(new Insets(0, 0, 0, 25));

        orderBox.getChildren().add(todoBox);

        // =====================================================
        // AGGIORNAMENTO DELLE STELLE
        // =====================================================
        renderStars(scoreStars);
    }

    // =========================================================
    // CREA INGREDIENTE
    // =========================================================
    private Node createIngredientNode(IngredientType ingredient, boolean isBaguette) {
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
        return ingredient == IngredientType.HAM
                ? "/assets/ingredient_ham.png"
                : "/assets/ingredient_" + ingredient.name().toLowerCase() + ".png";
    }

    private Label createOptionLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #4A3B32;"
        );
        return label;
    }

    // =========================================================
    // LAYOUT DINAMICO
    // =========================================================
    private void layoutLayers(List<Node> layers, boolean isBaguette) {
        double[] currentY = {-55.0}; // valore per spostare l'intero panino dentro il display

        IntStream.range(0, layers.size()).forEach(i -> {
            Node node = layers.get(i);
            node.setTranslateY(currentY[0]);
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
            currentY[0] += getLayerStep(node, isBaguette);
        });
    }

    // =========================================================
    // DISTANZA SPECIFICA PER INGREDIENTE
    // =========================================================
    private double getLayerStep(Node node, boolean isBaguette) {
        return Optional.ofNullable(node.getUserData())
                .filter(IngredientType.class::isInstance)
                .map(IngredientType.class::cast)
                .map(ingredient -> switch (ingredient) {
                    case LETTUCE, HAM -> 10.0;
                    case CHEESE -> 4.0;
                    case ONION -> 2.0;
                    case TOMATO -> 12.0;
                    case PICKLES -> 8.0;
                    case PATTY -> 12.2;
                    default -> 15.5;
                })
                .orElse(isBaguette ? 14.0 : 19.0); // distanziare il bottom bun dal top bun (differenza con baguette)
    }

    // =========================================================
    // STELLE: METHODS
    // =========================================================
    private void renderStars(int score) {
        int filledStars = clamp(score);

        IntStream.range(0, stars.length).forEach(i -> {
            boolean isFilled = i < filledStars;
            stars[i].setText(isFilled ? "★" : "☆");
            stars[i].setTextFill(isFilled ? Color.web("#f2b632") : Color.web("#d6b36a"));
        });
    }

    private int clamp(int value) {
        return Math.clamp(value, 0, STAR_COUNT);
    }

    // =========================================================
    // IMAGE VIEW
    // =========================================================
    private ImageView createImageView(String resourcePath, double width, double height) {
        ImageView imageView = new ImageView();

        Optional.ofNullable(getClass().getResource(resourcePath))
                .ifPresentOrElse(
                        imageUrl -> imageView.setImage(new Image(imageUrl.toExternalForm())),
                        () -> System.err.println("Immagine non trovata: " + resourcePath)
                );

        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);

        return imageView;
    }
}
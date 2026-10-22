package com.project.burger.view;

import com.project.burger.model.BreadShape;
import com.project.burger.model.Burger;
import com.project.burger.model.Ingredient;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.shape.Line;
import javafx.scene.Group;
import javafx.util.Duration;

import java.net.URL;

public class BurgerView extends StackPane {
    private static final double STANDARD_WIDTH = 140;
    private static final double BAGUETTE_WIDTH = 260;
    private static final double INGREDIENT_HEIGHT = 35;
    private static final double BELT_WIDTH = 900;
    private static final double BELT_HEIGHT = 38;
    private static final double TRAY_WIDTH = 220;
    private static final double TRAY_HEIGHT = 42;
    private static final int STATION_COUNT = 6;
    private static final double[] DEFAULT_STATION_X = {70, 194, 318, 442, 566, 690};

    private final Group beltLines = new Group();
    private final StackPane movingStack = new StackPane();
    private final VBox burgerLayers = new VBox(-5);
    private final double[] stationX = DEFAULT_STATION_X.clone();
    private int currentStation;
    private TranslateTransition stackTransition;
    private TranslateTransition beltTransition;

    public BurgerView() {
        setPrefSize(BELT_WIDTH, 250);
        setMinSize(BELT_WIDTH, 250);
        setMaxSize(BELT_WIDTH, 250);

        Pane belt = createConveyorBelt();
        StackPane.setAlignment(belt, Pos.BOTTOM_CENTER);

        movingStack.setPrefSize(BELT_WIDTH, 180);
        movingStack.setMinSize(BELT_WIDTH, 180);
        movingStack.setMaxSize(BELT_WIDTH, 180);
        movingStack.setAlignment(Pos.BOTTOM_CENTER);
        StackPane.setAlignment(movingStack, Pos.BOTTOM_CENTER);

        Node tray = createTray();
        StackPane trayAndBurger = new StackPane(tray, burgerLayers);
        trayAndBurger.setAlignment(Pos.BOTTOM_CENTER);
        StackPane.setMargin(burgerLayers, new javafx.geometry.Insets(0, 0, TRAY_HEIGHT - 8, 0));
        movingStack.getChildren().add(trayAndBurger);

        getChildren().addAll(belt, movingStack);
        moveStackTo(stationX[0], false);
    }

    private Pane createConveyorBelt() {
        Pane belt = new Pane();
        belt.setPrefSize(BELT_WIDTH, BELT_HEIGHT + 10); // +10 per lo spessore 3D

        // 1. Superficie piana superiore del nastro (dove appoggia il vassoio)
        // Rectangle surface = new Rectangle(BELT_WIDTH, BELT_HEIGHT);

        double slantOffsetLeft = 70.0; // più è alto, più si stringe in alto
        double slantOffsetRight = 0.0;

        Polygon surface = new Polygon();
        surface.getPoints().addAll(
                slantOffsetLeft, 0.0,      // Angolo in alto a sinistra (rientrante)
                BELT_WIDTH - slantOffsetRight, 0.0,   // Angolo in alto a destra (rientrante)
                BELT_WIDTH, BELT_HEIGHT,         // Angolo in basso a destra (largo)
                0.0, BELT_HEIGHT                 // Angolo in basso a sinistra (largo)
        );

        surface.setFill(Color.web("#5A5A5A")); // Grigio più chiaro per la parte piatta
        surface.setStroke(Color.web("#333333"));
        surface.setStrokeWidth(2);
        surface.setStrokeType(StrokeType.INSIDE);

        // 2. Spessore frontale (l'ombra/bordo del nastro per il 3D)
        Rectangle frontEdge = new Rectangle(BELT_WIDTH, 10);
        frontEdge.setTranslateY(BELT_HEIGHT); // Posizionato sotto la superficie
        frontEdge.setFill(Color.web("#2B2B2B")); // Molto scuro

        // maschera di ritaglio per le linee di scorrimento
        Polygon clipPolygon = new Polygon();
        clipPolygon.getPoints().addAll(
                slantOffsetLeft, 0.0,
                BELT_WIDTH - slantOffsetRight, 0.0,
                BELT_WIDTH, BELT_HEIGHT,
                0.0, BELT_HEIGHT
        );
        beltLines.setClip(clipPolygon);

        // Linee di scorrimento
        for (double x = -200; x < BELT_WIDTH + 200; x += 100) { // 100 è la distanza tra le linee
            double topX = x + slantOffsetLeft; // Inclinazione che segue esattamente il trapezio
            Line line = new Line(topX, 0, x, BELT_HEIGHT);
            line.setStroke(Color.web("#444444"));
            line.setStrokeWidth(2);
            beltLines.getChildren().add(line);
        }

        belt.getChildren().addAll(surface, frontEdge, beltLines);
        return belt;
    }

    private Node createTray() {
        ImageView tray = createImageView("/assets/tray.png", TRAY_WIDTH, TRAY_HEIGHT);
        if (tray.getImage() == null) {
            Rectangle fallback = new Rectangle(TRAY_WIDTH, TRAY_HEIGHT, Color.web("#777777"));
            fallback.setStroke(Color.web("#333333"));
            fallback.setArcWidth(12);
            fallback.setArcHeight(12);
            return new StackPane(fallback);
        }
        return tray;
    }

    /**
     * Esegue l'animazione delle scanalature del nastro solo una volta per la distanza richiesta.
     */
    public void animateConveyor(boolean forward) {
        if (beltTransition != null) {
            beltTransition.stop();
        }
        // Usa la stessa durata del vassoio (450ms) per sincronizzare il movimento
        beltTransition = new TranslateTransition(Duration.millis(450), beltLines);
        beltTransition.setByX(forward ? -124 : 124); // Muove le linee in base alla direzione
        beltTransition.setCycleCount(1); // Esegue una sola volta
        beltTransition.setInterpolator(Interpolator.EASE_BOTH);
        beltTransition.play();
    }

    /**
     * Supplies the actual dispenser center positions in this view's coordinate system.
     */
    public void setStationCenters(double... centers) {
        if (centers == null || centers.length != STATION_COUNT) {
            throw new IllegalArgumentException("Exactly six station centers are required");
        }
        System.arraycopy(centers, 0, stationX, 0, STATION_COUNT);
        moveStackTo(stationX[currentStation], false);
    }

    public void moveConveyorNext() {
        moveConveyorTo(Math.min(currentStation + 1, STATION_COUNT - 1), true);
    }

    public void moveConveyorPrevious() {
        moveConveyorTo(Math.max(currentStation - 1, 0), false);
    }

    public void moveConveyorTo(int stationIndex) {
        if (stationIndex < 0 || stationIndex >= STATION_COUNT) {
            throw new IllegalArgumentException("Station index must be between 0 and 5");
        }
        moveConveyorTo(stationIndex, stationIndex > currentStation);
    }

    private void moveConveyorTo(int stationIndex, boolean forward) {
        if (stationIndex == currentStation) {
            return;
        }
        currentStation = stationIndex;
        animateConveyor(forward);
        moveStackTo(stationX[currentStation], true);
    }

    private void moveStackTo(double targetX, boolean animated) {
        if (stackTransition != null) {
            stackTransition.stop();
        }
        double targetTranslateX = targetX - BELT_WIDTH / 2;
        if (!animated) {
            movingStack.setTranslateX(targetTranslateX);
            return;
        }
        stackTransition = new TranslateTransition(Duration.millis(450), movingStack);
        stackTransition.setToX(targetTranslateX);
        stackTransition.setInterpolator(Interpolator.EASE_BOTH);
        stackTransition.play();
    }

    public void renderBurger(Burger burger) {
        burgerLayers.getChildren().clear();
        if (burger == null) {
            return;
        }

        BreadShape shape = burger.getShape() == null ? BreadShape.CLASSIC : burger.getShape();
        boolean isBaguette = shape == BreadShape.BAGUETTE;
        burgerLayers.getChildren().add(createImageView(
                "/assets/bun_" + shape.name().toLowerCase() + "_bottom.png",
                isBaguette ? BAGUETTE_WIDTH : STANDARD_WIDTH, 45));

        for (Ingredient ingredient : burger.getIngredients()) {
            burgerLayers.getChildren().add(createIngredientNode(
                    ingredientImagePath(ingredient.getType()), isBaguette, INGREDIENT_HEIGHT));
        }
        if (burger.getSauce() != null) {
            burgerLayers.getChildren().add(createIngredientNode(
                    ingredientImagePath(burger.getSauce()), isBaguette, 20));
        }
        burgerLayers.getChildren().add(createImageView(
                "/assets/bun_" + shape.name().toLowerCase() + "_top.png",
                isBaguette ? BAGUETTE_WIDTH : STANDARD_WIDTH, 45));
        if (burger.isFriesPresent()) {
            burgerLayers.getChildren().add(createImageView("/assets/fries.png", STANDARD_WIDTH, 45));
        }
    }

    private Node createIngredientNode(String resourcePath, boolean isBaguette, double height) {
        return createImageView(resourcePath, isBaguette ? BAGUETTE_WIDTH : STANDARD_WIDTH, height);
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

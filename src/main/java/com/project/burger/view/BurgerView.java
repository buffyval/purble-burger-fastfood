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
    private static final double BELT_HEIGHT = 50;
    private static final double TRAY_WIDTH = 130;
    private static final double TRAY_HEIGHT = 30;
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
        movingStack.setAlignment(Pos.BOTTOM_LEFT);
        StackPane.setAlignment(movingStack, Pos.BOTTOM_LEFT);

        movingStack.setTranslateY(-135); // aumenta per allineare il vassoio con il nastro

        Node tray = createTray();

        /*
         * Larghezza visiva reale del vassoio:
         */
        final double TRAY_VISUAL_WIDTH = TRAY_WIDTH + 40;

        /*
         * Contenitore del panino.
         */
        StackPane burgerContainer = new StackPane(burgerLayers);

        burgerContainer.setPrefWidth(STANDARD_WIDTH);
        burgerContainer.setMinWidth(STANDARD_WIDTH);
        burgerContainer.setMaxWidth(STANDARD_WIDTH);

        burgerContainer.setAlignment(Pos.BOTTOM_CENTER);

        final double BURGER_X_OFFSET = 40;
        final double BURGER_Y_OFFSET = 20;
        double centeredBurgerX = (TRAY_VISUAL_WIDTH - STANDARD_WIDTH) / 2;
        burgerContainer.setTranslateX (centeredBurgerX + BURGER_X_OFFSET);
        burgerContainer.setTranslateY(BURGER_Y_OFFSET);

        StackPane.setMargin(burgerContainer, new javafx.geometry.Insets(0, 0, BURGER_Y_OFFSET, 0));

        /*
         * Contenitore comune di vassoio e panino.
         * Fondamentale: blocchiamo la larghezza.
         */
        StackPane trayAndBurger = new StackPane();

        trayAndBurger.setPrefSize(TRAY_VISUAL_WIDTH, 180);
        trayAndBurger.setMinSize(TRAY_VISUAL_WIDTH, 180);
        trayAndBurger.setMaxSize(TRAY_VISUAL_WIDTH, 180);

        /*
         * Allineamento specifico dei due elementi.
         */
        StackPane.setAlignment(tray, Pos.BOTTOM_LEFT);
        StackPane.setAlignment(burgerContainer, Pos.BOTTOM_LEFT);

        trayAndBurger.getChildren().addAll(tray, burgerContainer);

        /*
         * Posizioniamo l'intero gruppo vassoio + panino
         * sul lato sinistro del movingStack.
         */
        StackPane.setAlignment(trayAndBurger, Pos.BOTTOM_LEFT);

        movingStack.getChildren().add(trayAndBurger);

        getChildren().addAll(belt, movingStack);
        moveStackTo(stationX[0], false);
    }

    private Pane createConveyorBelt() {
        Pane belt = new Pane();
        belt.setPrefSize(BELT_WIDTH, BELT_HEIGHT + 10); // +10 per lo spessore 3D

        double slantOffsetLeft = 70.0; // più è alto, più si stringe in alto
        double slantOffsetRight = 0.0;

        Polygon surface = new Polygon();
        surface.getPoints().addAll(
                slantOffsetLeft, 0.0,      // Angolo in alto a sinistra (rientrante)
                BELT_WIDTH - slantOffsetRight, 0.0,  // Angolo in alto a destra (rientrante)
                BELT_WIDTH, BELT_HEIGHT,             // Angolo in basso a destra (largo)
                0.0, BELT_HEIGHT                     // Angolo in basso a sinistra (largo)
        );

        surface.setFill(Color.web("#5A5A5A")); // Grigio più chiaro per la parte piatta
        surface.setStroke(Color.web("#333333"));
        surface.setStrokeWidth(2);
        surface.setStrokeType(StrokeType.INSIDE);

        // Spessore frontale (l'ombra/bordo del nastro per il 3D)
        Rectangle frontEdge = new Rectangle(BELT_WIDTH, 10);
        frontEdge.setTranslateY(BELT_HEIGHT); // Posizionato sotto la superficie
        frontEdge.setFill(Color.web("#2B2B2B"));

        // maschera di ritaglio per le linee di scorrimento
        Polygon clipPolygon = new Polygon();
        clipPolygon.getPoints().addAll(
                slantOffsetLeft, 0.0,
                BELT_WIDTH - slantOffsetRight, 0.0,
                BELT_WIDTH, BELT_HEIGHT,
                0.0, BELT_HEIGHT
        );
        beltLines.setClip(clipPolygon); // maschera di ritaglio

        // Linee di scorrimento
        for (double x = -1000; x < BELT_WIDTH + 1000; x += 200) {
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
        Polygon trayPolygon = new Polygon();

        double traySlant = 40.0; // inclinazione del parallelogramma (vassoio)

        // Punti del Parallelogramma (inclinato verso destra)
        trayPolygon.getPoints().addAll(
                traySlant, 0.0,                 // 1. In alto a sinistra (spostato a destra di 40px)
                TRAY_WIDTH + traySlant, 0.0,     // 2. In alto a destra (spostato a destra di 40px)
                TRAY_WIDTH, TRAY_HEIGHT,         // 3. In basso a destra
                0.0, TRAY_HEIGHT                 // 4. In basso a sinistra
        );

        trayPolygon.setFill(Color.web("#777777"));
        trayPolygon.setStroke(Color.web("#333333"));
        trayPolygon.setStrokeWidth(2);

        return new StackPane(trayPolygon);
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
        beltTransition.setByX(forward ? 200 : -200); // Muove le linee in base alla direzione
        beltTransition.setCycleCount(1); // Esegue una sola volta
        beltTransition.setInterpolator(Interpolator.EASE_BOTH);
        beltTransition.setOnFinished(e -> beltLines.setTranslateX(0));
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
        final double TRAY_X_OFFSET = 40;
        double targetTranslateX = targetX - (TRAY_WIDTH / 2) + TRAY_X_OFFSET;
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
                "/assets/bun_" + shape.name().toLowerCase() + "_top.png",
                isBaguette ? BAGUETTE_WIDTH : STANDARD_WIDTH, 45));

        // extra ingredients
        for (Ingredient ingredient : burger.getIngredients()) {
            burgerLayers.getChildren().add(createIngredientNode(
                    ingredientImagePath(ingredient.getType()), isBaguette, INGREDIENT_HEIGHT));
        }

        // sauce
        if (burger.getSauce() != null) {
            burgerLayers.getChildren().add(createIngredientNode(
                    ingredientImagePath(burger.getSauce()), isBaguette, 20));
        }

        burgerLayers.getChildren().add(createImageView(
                "/assets/bun_" + shape.name().toLowerCase() + "_bottom.png",
                isBaguette ? BAGUETTE_WIDTH : STANDARD_WIDTH, 45));

        // fries are optional
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

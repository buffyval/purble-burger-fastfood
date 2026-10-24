package com.project.burger.view;

import com.project.burger.controller.MainController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;

public class MainApp extends Application {

    private static final double GAME_WIDTH = 1000;
    private static final double GAME_HEIGHT = 700;

    @Override
    public void start(Stage primaryStage) {
        MainController controller = new MainController();
        OrderDisplayView orderDisplayView = new OrderDisplayView();
        BurgerView burgerView = new BurgerView();
        ChefView chefView = new ChefView();

        Runnable refresh = () -> {
            burgerView.renderBurger(controller.getCurrentBurger());
            orderDisplayView.renderOrder(
                    controller.getGameEngine().getCurrentOrder(),
                    controller.getGameEngine().getCurrentBurgerCount(),
                    controller.getGameEngine().getScoreStars());
        };

        StationsView stationsView = new StationsView(controller.getStationController());

        Button previousButton = new Button("<");
        previousButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 8 12; -fx-cursor: hand;");
        previousButton.setOnAction(event -> {
            controller.moveConveyorPrevious();
            burgerView.moveConveyorTo(controller.getConveyorController().getCurrentStationIndex());
            refresh.run();
        });

        Button nextButton = new Button(">");
        nextButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 8 12; -fx-cursor: hand;");
        nextButton.setOnAction(event -> {
            controller.moveConveyorNext();
            burgerView.moveConveyorTo(controller.getConveyorController().getCurrentStationIndex());
            refresh.run();
        });

        HBox bottomControls = new HBox(12, previousButton, stationsView, nextButton);
        bottomControls.setAlignment(Pos.CENTER);
        bottomControls.toFront();

        BorderPane gameLayout = new BorderPane();
        gameLayout.setBackground(Background.EMPTY);

        // Menu di Start Iniziale (Titolo + Pulsante centrale)
        Label titleLabel = new Label("Purble Burger Fast Food");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Button startButton = new Button("Start Game");
        startButton.setPrefSize(220, 60);
        startButton.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand;");

        VBox startMenuBox = new VBox(25, titleLabel, startButton);
        startMenuBox.setAlignment(Pos.CENTER);
        startMenuBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-padding: 40; -fx-background-radius: 15;");
        startMenuBox.setMaxSize(500, 250);

        gameLayout.setCenter(startMenuBox);

        StackPane root = new StackPane(gameLayout);

        // 1. SFONDO START FISSO (kitchen_background.png - dimensione fissa 1000x700 non flessibile)
        setFixedAppBackground(root, "/assets/kitchen_background.png");

        // 2. AZIONE AL CLICK SU START GAME
        startButton.setOnAction(event -> {
            controller.handleStartGame();

            // SFONDO WORKBENCH FISSO (background_workbench.png - dimensione fissa 1000x700 non flessibile)
            setFixedAppBackground(root, "/assets/background_workbench.png");

            // Svuotiamo il menu di avvio al centro
            gameLayout.setCenter(null);

            // --- LAYOUT INTERFACCIA A COORDINATE ANCORATE ---
            Pane gamePane = new Pane();
            gamePane.setPrefSize(GAME_WIDTH, GAME_HEIGHT);
            gamePane.setMaxSize(GAME_WIDTH, GAME_HEIGHT);

            // ----- DISPLAY DELL'ORDINE -----
            // in basso a sinistra dentro l'immagine
            orderDisplayView.setMaxSize(200, 90);
            orderDisplayView.setLayoutX(5);
            orderDisplayView.setLayoutY(350);

            // ----- GRUPPO STAZIONI + FRECCE -----
            // Al centro sulla superficie del banco
            HBox stationsWithArrows = new HBox(10, previousButton, stationsView, nextButton);
            stationsWithArrows.setAlignment(Pos.CENTER);
            stationsWithArrows.setLayoutX(270); // modifica la pos X sulla workbench
            stationsWithArrows.setLayoutY(400); // Y su workbench

            // ----- DISPENSERS -----
            // Sopra il gruppo di bottoni delle stazioni
            HBox dispensersBox = new HBox(0); // Spazio tra una macchina e l'altra (a 0, gestita nello switch)
            dispensersBox.setAlignment(Pos.BOTTOM_CENTER);

            String[] machinePaths = {
                    "/assets/machine_shape.png",
                    "/assets/machine_meat.png",
                    "/assets/machine_veggies.png",
                    "/assets/machine_pickles_cheese.png",
                    "/assets/machine_fries_sauces.png"
            };

            for (String path : machinePaths) {
                ImageView machineView = new ImageView();
                URL url = getClass().getResource(path);
                if (url != null) {
                    machineView.setImage(new Image(url.toExternalForm()));
                } else {
                    System.err.println("Image not found: " + path);
                }
                machineView.setFitHeight(220); // Modifica la dimensione di tutti i dispenser qui
                machineView.setPreserveRatio(true);

                switch (path) {
                    case "/assets/machine_shape.png" ->
                            machineView.setTranslateY(-1); // valore negativo alza (Y)
                    case "/assets/machine_meat.png" ->
                            machineView.setTranslateX(-60); // Valore negativo (X) <- verso sinistra
                    case "/assets/machine_veggies.png" ->
                            machineView.setTranslateX(-110);
                    case "/assets/machine_pickles_cheese.png" -> {
                        machineView.setTranslateY(3);
                        machineView.setTranslateX(-160);
                    }
                    case "/assets/machine_fries_sauces.png" -> {
                        machineView.setTranslateY(3);
                        machineView.setTranslateX(-240);
                    }
                }

                dispensersBox.getChildren().add(machineView);
            }

            // posizioni globali dei dispenser
            dispensersBox.setLayoutX(165); // aumenta per spostare a destra
            dispensersBox.setLayoutY(48); // aumenta per spostare in basso

            // ----- CHEF + CONVEYOR -----
            chefView.setLayoutX(30);
            chefView.setLayoutY(360); // Sposta lo chef più in basso se necessario

            burgerView.setLayoutX(100); // Aumenta per spostare a destra
            burgerView.setLayoutY(252); // Aumenta il valore Y per abbassare
            // burgerView.animateConveyor(true);

            // Aggiungo i controlli al gamePane (burgerView inserito PRIMA di stationsWithArrows)
            gamePane.getChildren().addAll(orderDisplayView, dispensersBox, chefView, burgerView, stationsWithArrows);
            stationsWithArrows.toFront();

            // Inserisco il gamePane al centro della finestra
            gameLayout.setCenter(gamePane);

            refresh.run();
        });

        primaryStage.setTitle("Purble Burger Fast Food");
        primaryStage.setScene(new Scene(root, GAME_WIDTH, GAME_HEIGHT));
        primaryStage.setMinWidth(GAME_WIDTH);
        primaryStage.setMinHeight(GAME_HEIGHT);
        primaryStage.show();
    }

    /**
     * Imposta uno sfondo a dimensione fissa (1000x700) centrato, SENZA ridimensionamento
     * all'ingrandimento della finestra.
     */
    private void setFixedAppBackground(Pane rootPane, String resourcePath) {
        URL backgroundUrl = getClass().getResource(resourcePath);
        if (backgroundUrl == null && resourcePath.contains("/assets/")) {
            backgroundUrl = getClass().getResource(resourcePath.replace("/assets/", "/images/"));
        }

        if (backgroundUrl != null) {
            Image backgroundImage = new Image(backgroundUrl.toExternalForm());

            // Dimensione fissa 1000x700 (widthAsPercentage=false, heightAsPercentage=false, contain=false, cover=false)
            BackgroundSize fixedSize = new BackgroundSize(
                    GAME_WIDTH, GAME_HEIGHT, false, false, false, false);

            rootPane.setBackground(new Background(new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    fixedSize)));
        } else {
            rootPane.setStyle("-fx-background-color: #f4f4f4;");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
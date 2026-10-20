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

        // TEMPORANEO: Nascondiamo il panino finché non riallineiamo le quote Y del nastro
        burgerView.setVisible(false);

        Runnable refresh = () -> {
            burgerView.renderBurger(controller.getCurrentBurger());
            orderDisplayView.renderOrder(
                    controller.getGameEngine().getCurrentOrder(),
                    controller.getGameEngine().getCurrentBurgerCount(),
                    controller.getGameEngine().getScoreStars());
        };

        StationsView stationsView = new StationsView(controller.getStationController());

        Button previousButton = new Button("<");
        previousButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 8 12;");
        previousButton.setOnAction(event -> {
            controller.moveConveyorPrevious();
            refresh.run();
        });

        Button nextButton = new Button(">");
        nextButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 8 12;");
        nextButton.setOnAction(event -> {
            controller.moveConveyorNext();
            refresh.run();
        });

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

            // A. DISPLAY DELL'ORDINE: in basso a sinistra dentro l'immagine
            orderDisplayView.setMaxSize(200, 90);
            orderDisplayView.setLayoutX(5);
            orderDisplayView.setLayoutY(350);

            // B. GRUPPO STAZIONI + FRECCE: Al centro sulla superficie del banco
            HBox stationsWithArrows = new HBox(10, previousButton, stationsView, nextButton);
            stationsWithArrows.setAlignment(Pos.CENTER);
            stationsWithArrows.setLayoutX(270); // modifica la pos X sulla workbench
            stationsWithArrows.setLayoutY(400); // Y su workbench

            // C. DISPENSER MACCHINARI: Sopra il gruppo di bottoni delle stazioni
            HBox dispensersBox = new HBox(-30); // Spazio tra una macchina e l'altra
            dispensersBox.setAlignment(Pos.CENTER);

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
                    System.err.println("Immagine non trovata: " + path);
                }
                machineView.setFitHeight(220); // Modifica la dimensione di tutti i dispenser qui
                machineView.setPreserveRatio(true);
                dispensersBox.getChildren().add(machineView);
            }

            dispensersBox.setLayoutX(170); // aumenta per spostare a destra
            dispensersBox.setLayoutY(50); // aumenta per spostare in basso

            // C. CHEF + PANINO: Sul piano della cucina
            HBox upperCenter = new HBox(30, chefView, burgerView);
            upperCenter.setAlignment(Pos.CENTER);
            upperCenter.setLayoutX(300);
            upperCenter.setLayoutY(180);

            // Aggiungo i controlli al gamePane
            gamePane.getChildren().addAll(orderDisplayView, dispensersBox, stationsWithArrows, upperCenter);

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
package com.project.burger.view;

import com.project.burger.controller.MainController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainController controller = new MainController();
        OrderDisplayView orderDisplayView = new OrderDisplayView();
        BurgerView burgerView = new BurgerView();
        ChefView chefView = new ChefView();

        // TEMPORANEO: Nascondiamo il panino sul nastro finché non riallineiamo le quote Y
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
        previousButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10 15;");
        previousButton.setOnAction(event -> {
            controller.moveConveyorPrevious();
            refresh.run();
        });

        Button nextButton = new Button(">");
        nextButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10 15;");
        nextButton.setOnAction(event -> {
            controller.moveConveyorNext();
            refresh.run();
        });

        BorderPane gameLayout = new BorderPane();
        gameLayout.setBackground(Background.EMPTY);

        // Menu di Start Iniziale
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

        // 1. Sfondo Schermata Start
        setAppBackground(root, "/assets/kitchen_background.png");

        // 2. Azione al click su "Start Game"
        startButton.setOnAction(event -> {
            controller.handleStartGame();

            // Sostituzione sfondo con la Workbench
            setAppBackground(root, "/assets/background_workbench.png");

            // --- NUOVO LAYOUT DEL BANCO DI LAVORO (WORKBENCH) ---

            // Area centrale superiore (Chef + Nastro con Panino nascosto temporaneamente)
            HBox upperCenter = new HBox(30, chefView, burgerView);
            upperCenter.setAlignment(Pos.CENTER);

            // Ridimensionamento e contenimento del Display Ordine
            orderDisplayView.setMaxSize(200, 220);

            // Barra dei controlli frontale (Workbench):
            // - A sinistra: Display dell'ordine (spostato in basso sul lato del workbench)
            // - Al centro: Frecce del nastro e Pulsanti di assemblaggio
            HBox workbenchFront = new HBox(15);
            workbenchFront.setAlignment(Pos.BOTTOM_CENTER);
            workbenchFront.setPadding(new Insets(0, 10, 15, 10));

            HBox stationsWithArrows = new HBox(10, previousButton, stationsView, nextButton);
            stationsWithArrows.setAlignment(Pos.CENTER);

            // Organizziamo il layout frontale: Display a sinistra, Stazioni al centro
            BorderPane bottomWorkbenchBar = new BorderPane();
            bottomWorkbenchBar.setPadding(new Insets(0, 20, 30, 20));
            bottomWorkbenchBar.setLeft(orderDisplayView);
            bottomWorkbenchBar.setCenter(stationsWithArrows);
            BorderPane.setAlignment(orderDisplayView, Pos.BOTTOM_LEFT);
            BorderPane.setAlignment(stationsWithArrows, Pos.BOTTOM_CENTER);

            gameLayout.setTop(null); // Svuotiamo la parte superiore
            gameLayout.setCenter(upperCenter);
            gameLayout.setBottom(bottomWorkbenchBar);

            refresh.run();
        });

        primaryStage.setTitle("Purble Burger Fast Food");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }

    private void setAppBackground(Pane rootPane, String resourcePath) {
        URL backgroundUrl = getClass().getResource(resourcePath);
        if (backgroundUrl == null && resourcePath.contains("/assets/")) {
            backgroundUrl = getClass().getResource(resourcePath.replace("/assets/", "/images/"));
        }

        if (backgroundUrl != null) {
            Image backgroundImage = new Image(backgroundUrl.toExternalForm());
            BackgroundSize bgSize = new BackgroundSize(1000, 700, false, false, false, true);

            rootPane.setBackground(new Background(new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    bgSize)));
        } else {
            rootPane.setStyle("-fx-background-color: #f4f4f4;");
        }
    }

}
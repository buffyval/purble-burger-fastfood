package com.project.burger.view;

import com.project.burger.controller.MainController;
import javafx.application.Application;
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
        // Rende il layout del gioco trasparente per lasciar trasparire lo sfondo sotto
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

        // All'avvio il gioco mostra SOLO il menu di start al centro
        gameLayout.setCenter(startMenuBox);

        // Quando si clicca "Start Game", appaiono l'ordine, lo chef, il panino e i pulsanti
        startButton.setOnAction(event -> {
            controller.handleStartGame();

            // Costruzione del layout di gioco attivo
            HBox centerGame = new HBox(30, chefView, burgerView);
            centerGame.setAlignment(Pos.CENTER);

            HBox bottomControls = new HBox(12, previousButton, stationsView, nextButton);
            bottomControls.setAlignment(Pos.CENTER);

            gameLayout.setTop(orderDisplayView);
            gameLayout.setCenter(centerGame);
            gameLayout.setBottom(bottomControls);

            refresh.run();
        });

        StackPane root = new StackPane(gameLayout);

        // Caricamento Sfondo della Cucina (Controlla sia /assets/ che /images/)
        URL backgroundUrl = getClass().getResource("/assets/kitchen_background.png");
        if (backgroundUrl == null) {
            backgroundUrl = getClass().getResource("/images/kitchen_background.png");
        }

        if (backgroundUrl != null) {
            Image backgroundImage = new Image(backgroundUrl.toExternalForm());
            root.setBackground(new Background(new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(1.0, 1.0, true, true, false, true))));
        } else {
            root.setStyle("-fx-background-color: #f4f4f4;");
        }

        primaryStage.setTitle("Purble Burger Fast Food");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
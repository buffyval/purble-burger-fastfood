package com.project.burger.view;

import com.project.burger.controller.MainController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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

        StationsView stationsView = new StationsView(
                controller.getStationController(), refresh);

        Button startButton = new Button("Start Game");
        startButton.setOnAction(event -> {
            controller.handleStartGame();
            refresh.run();
        });

        Button previousButton = new Button("<");
        previousButton.setOnAction(event -> controller.moveConveyorPrevious());
        Button nextButton = new Button(">");
        nextButton.setOnAction(event -> controller.moveConveyorNext());

        HBox center = new HBox(30, chefView, burgerView);
        center.setAlignment(Pos.CENTER);

        HBox bottom = new HBox(12, previousButton, stationsView, nextButton);
        bottom.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setTop(new HBox(20, startButton, orderDisplayView));
        root.setCenter(center);
        root.setBottom(bottom);

        refresh.run();
        primaryStage.setTitle("Purble Burger Fast Food");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

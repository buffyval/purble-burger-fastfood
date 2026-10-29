package com.project.burger.view;

import com.project.burger.controller.ConveyorController;
import com.project.burger.controller.StationController;
import com.project.burger.model.BreadShape;
import com.project.burger.model.IngredientType;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;

public class StationsView extends HBox {

    private final Runnable refreshCallback;
    private final ConveyorController conveyorController;

    private Runnable onRestartCallback;

    public StationsView(StationController stationController, ConveyorController conveyorController, Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
        this.conveyorController = conveyorController;

        setAlignment(Pos.CENTER);
        setSpacing(12); // increase for more spacing

        // 1. Shape (Classic, Baguette, Bagel)
        getChildren().add(createStation("Buns", createShapeButtons(stationController)));

        // 2. Meat (Carne & Prosciutto)
        Button pattyButton = createImageButton("/assets/ingredient_patty.png");
        pattyButton.setOnAction(event -> {
            if (getCurrentStation() == 1) {
                stationController.addAddition(IngredientType.PATTY);
                triggerRefresh();
            }
        });

        Button hamButton = createImageButton("/assets/ingredient_ham.png");
        hamButton.setOnAction(event -> {
            if (getCurrentStation() == 1) {
                stationController.addAddition(IngredientType.HAM);
                triggerRefresh();
            }
        });

        VBox meatGroup = new VBox(5, pattyButton, hamButton);
        meatGroup.setAlignment(Pos.CENTER);

        getChildren().add(createStation("Meat", meatGroup));

        // 3. Additions 1 (Verdure: Lattuga, Cipolla, Pomodoro) -> Stazione 2
        getChildren().add(createStation("Veggies", createVeggieButtons(stationController)));

        // 4. Additions 2 (Pickles / Cheese) -> Stazione 3
        getChildren().add(createStation("Additions", createCheesePicklesButtons(stationController)));

        // 5. Fries & Sauces -> Stazione 4
        Button friesButton = createImageButton("/assets/fries.png");
        friesButton.setOnAction(event -> {
            if (getCurrentStation() == 4) {
                stationController.addFries();
                triggerRefresh();
            }
        });

        Button ketchupButton = createImageButton("/assets/sauce_ketchup.png");
        ketchupButton.setOnAction(event -> {
            if (getCurrentStation() == 4) {
                stationController.applySauce(IngredientType.KETCHUP);
                triggerRefresh();
            }
        });

        Button mustardButton = createImageButton("/assets/sauce_mustard.png");
        mustardButton.setOnAction(event -> {
            if (getCurrentStation() == 4) { // Corretto sintassi con 'if'
                stationController.applySauce(IngredientType.MUSTARD);
                triggerRefresh();
            }
        });

        VBox saucesGroup = new VBox(5, ketchupButton, mustardButton);
        saucesGroup.setAlignment(Pos.CENTER);

        VBox friesAndSaucesGroup = new VBox(8, friesButton, saucesGroup);
        friesAndSaucesGroup.setAlignment(Pos.CENTER);
        getChildren().add(createStation("Fries & Dips", friesAndSaucesGroup));

        // 6. Deliver and Trash
        VBox actionButtonsGroup = getActionButtonsGroup(stationController);
        actionButtonsGroup.setStyle("-fx-background-color: rgba(255, 255, 255, 0.85); -fx-padding: 14 18; -fx-background-radius: 8;");
        getChildren().add(createStation("Actions", actionButtonsGroup));
    }

    public void setOnRestartCallback(Runnable onRestartCallback) { this.onRestartCallback = onRestartCallback; }
    
    private VBox getActionButtonsGroup(StationController stationController) {

        Button deliverButton = new Button("Deliver");
        deliverButton.setStyle("-fx-font-weight: bold; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 14px; -fx-padding: 8 16; -fx-background-radius: 6;");
        deliverButton.setOnAction(event -> {
            stationController.deliverBurger();
            triggerRefresh();
        });

        Button trashButton = new Button("Trash");
        trashButton.setStyle("-fx-font-weight: bold; -fx-background-color: #f44336; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 14px; -fx-padding: 8 16; -fx-background-radius: 6;");
        trashButton.setOnAction(event -> {
            stationController.resetBurger();
            triggerRefresh();
        });

        Button restartButton = new Button("Restart");
        restartButton.setStyle("-fx-font-weight: bold; -fx-background-color: #FFC107; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 14px; -fx-padding: 8 16; -fx-background-radius: 6;");
        restartButton.setOnAction(event -> {
            stationController.restartGame();
            conveyorController.reset();
            MainApp.isRestartRequested = true;
            if (onRestartCallback != null) {
                onRestartCallback.run();
            }
            triggerRefresh();
        });

        VBox actionButtonsGroup = new VBox(6, deliverButton, trashButton, restartButton);
        actionButtonsGroup.setAlignment(Pos.CENTER);
        return actionButtonsGroup;
    }

    private int getCurrentStation() {
        return conveyorController != null ? conveyorController.getCurrentStationIndex() : 0;
    }

    private void triggerRefresh() {
        if (refreshCallback != null) {
            refreshCallback.run();
        }
    }

    private VBox createStation(String name, javafx.scene.Node... controls) {
        VBox station = new VBox(5);
        station.setAlignment(Pos.CENTER);
        station.setStyle("-fx-background-color: #D3D3D3; -fx-padding: 8; -fx-background-radius: 8;");
        Label label = new Label(name);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");
        station.getChildren().add(label);
        station.getChildren().addAll(controls);
        return station;
    }

    private javafx.scene.Node[] createShapeButtons(StationController controller) {
        Button[] buttons = new Button[BreadShape.values().length];
        for (int i = 0; i < buttons.length; i++) {
            BreadShape shape = BreadShape.values()[i];
            buttons[i] = createImageButton("/assets/bun_" + shape.name().toLowerCase() + "_top.png");
            buttons[i].setOnAction(event -> {
                if (getCurrentStation() == 0) {
                    controller.applyShape(shape);
                    triggerRefresh();
                }
            });
        }
        return buttons;
    }

    private javafx.scene.Node[] createVeggieButtons(StationController controller) {
        return new javafx.scene.Node[]{
                createIngredientBtn(controller, IngredientType.LETTUCE, 2),
                createIngredientBtn(controller, IngredientType.ONION, 2),
                createIngredientBtn(controller, IngredientType.TOMATO, 2)
        };
    }

    private javafx.scene.Node[] createCheesePicklesButtons(StationController controller) {
        return new javafx.scene.Node[]{
                createIngredientBtn(controller, IngredientType.CHEESE, 3),
                createIngredientBtn(controller, IngredientType.PICKLES, 3)
        };
    }

    private Button createIngredientBtn(StationController controller, IngredientType type, int requiredStation) {
        Button button = createImageButton("/assets/ingredient_" + type.name().toLowerCase() + ".png");
        button.setOnAction(event -> {
            if (getCurrentStation() == requiredStation) {
                controller.addAddition(type);
                triggerRefresh();
            }
        });
        return button;
    }

    private Button createImageButton(String resourcePath) {
        Button button = new Button();
        URL imageUrl = getClass().getResource(resourcePath);
        if (imageUrl != null) {
            ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
            imageView.setFitHeight(50);
            imageView.setPreserveRatio(true);
            button.setGraphic(imageView);
        } else {
            String name = resourcePath.substring(resourcePath.lastIndexOf('/') + 1)
                    .replace(".png", "").replace("ingredient_", "").replace("bun_", "_top");
            button.setText(name);
        }
        button.setStyle("-fx-cursor: hand; -fx-background-color: #F5F5F5; -fx-border-color: #B0B0B0; -fx-border-radius: 4;");
        return button;
    }
}
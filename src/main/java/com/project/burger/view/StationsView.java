package com.project.burger.view;

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

    public StationsView(StationController stationController) {
        setAlignment(Pos.CENTER);
        setSpacing(10);

        // 1. Shape (Classic, Baguette, Bagel)
        getChildren().add(createStation("Shape", createShapeButtons(stationController)));

        // 2. Meat (Carne)
        Button meatButton = createImageButton("/assets/ingredient_patty.png", 35);
        meatButton.setOnAction(event -> stationController.addMeat());
        getChildren().add(createStation("Meat", meatButton));

        // 3. Additions 1 (Verdure: Lattuga, Cipolla, Pomodoro)
        getChildren().add(createStation("Additions", createVeggieButtons(stationController)));

        // 4. Additions 2 (Pickles / Cheese)
        getChildren().add(createStation("Additions", createCheesePicklesButtons(stationController)));

        // 5. Fries (Patatine)
        Button friesButton = createImageButton("/assets/fries.png", 35);
        friesButton.setOnAction(event -> stationController.addFries());
        getChildren().add(createStation("Fries", friesButton));

        // 6. Sauces (Salse)
        getChildren().add(createStation("Sauces", createSauceButtons(stationController)));

        // 7. Mechanic Arm (Braccio Meccanico)
        Button mechanicArmButton = new Button("Submit");
        mechanicArmButton.setStyle("-fx-font-weight: bold;");
        mechanicArmButton.setOnAction(event -> stationController.triggerMechanicArm(null));
        getChildren().add(createStation("Mechanic Arm", mechanicArmButton));
    }

    private VBox createStation(String name, javafx.scene.Node... controls) {
        VBox station = new VBox(5);
        station.setAlignment(Pos.CENTER);
        station.setStyle("-fx-background-color: rgba(255, 255, 255, 0.85); -fx-padding: 8; -fx-background-radius: 8;");
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
            buttons[i] = createImageButton("/assets/top_bun_" + shape.name().toLowerCase() + ".png", 30);
            buttons[i].setOnAction(event -> controller.applyShape(shape));
        }
        return buttons;
    }

    // Verdure: Lattuga, Cipolla, Pomodoro
    private javafx.scene.Node[] createVeggieButtons(StationController controller) {
        return new javafx.scene.Node[]{
                createIngredientBtn(controller, IngredientType.LETTUCE),
                createIngredientBtn(controller, IngredientType.ONION),
                createIngredientBtn(controller, IngredientType.TOMATO)
        };
    }

    // Formaggio e Cetriolini
    private javafx.scene.Node[] createCheesePicklesButtons(StationController controller) {
        return new javafx.scene.Node[]{
                createIngredientBtn(controller, IngredientType.CHEESE),
                createIngredientBtn(controller, IngredientType.PICKLES)
        };
    }

    // Salse
    private javafx.scene.Node[] createSauceButtons(StationController controller) {
        return new javafx.scene.Node[]{
                createIngredientBtn(controller, IngredientType.KETCHUP),
                createIngredientBtn(controller, IngredientType.MUSTARD),
                createIngredientBtn(controller, IngredientType.MAYONNAISE)
        };
    }

    private Button createIngredientBtn(StationController controller, IngredientType type) {
        Button button = createImageButton("/assets/ingredient_" + type.name().toLowerCase() + ".png", 28);
        button.setOnAction(event -> controller.addAddition(type));
        return button;
    }

    private Button createImageButton(String resourcePath, double height) {
        Button button = new Button();
        URL imageUrl = getClass().getResource(resourcePath);
        if (imageUrl != null) {
            ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(true);
            button.setGraphic(imageView);
        } else {
            String name = resourcePath.substring(resourcePath.lastIndexOf('/') + 1)
                    .replace(".png", "").replace("ingredient_", "").replace("top_bun_", "");
            button.setText(name);
        }
        button.setStyle("-fx-cursor: hand; -fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 4;");
        return button;
    }
}
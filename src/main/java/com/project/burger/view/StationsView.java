package com.project.burger.view;

import com.project.burger.controller.StationController;
import com.project.burger.model.BreadShape;
import com.project.burger.model.IngredientType;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.function.Consumer;

public class StationsView extends HBox {
    private final Runnable onChange;

    public StationsView(StationController stationController) {
        this(stationController, () -> {
        });
    }

    public StationsView(StationController stationController, Runnable onChange) {
        this.onChange = onChange;
        setAlignment(Pos.CENTER);
        setSpacing(12);

        Button meatButton = new Button("Add beef");
        meatButton.setOnAction(event -> {
            stationController.addMeat();
            onChange.run();
        });

        Button friesButton = new Button("Add fries");
        friesButton.setOnAction(event -> {
            stationController.addFries();
            onChange.run();
        });

        Button mechanicArmButton = new Button("Submit");
        mechanicArmButton.setOnAction(event -> {
            stationController.triggerMechanicArm();
            onChange.run();
        });

        getChildren().add(createStation("Shape", createShapeButtons(stationController)));
        getChildren().add(createStation("Meat", meatButton));
        getChildren().add(createStation("Additions", createAdditionButtons(stationController)));
        getChildren().add(createStation("Fries", friesButton));
        getChildren().add(createStation("Sauces", createSauceButtons(stationController)));
        getChildren().add(createStation("Mechanic Arm", mechanicArmButton));
    }

    private VBox createStation(String name, javafx.scene.Node... controls) {
        VBox station = new VBox(6);
        station.setAlignment(Pos.CENTER);
        station.getChildren().add(new Label(name));
        station.getChildren().addAll(controls);
        return station;
    }

    private javafx.scene.Node[] createShapeButtons(StationController controller) {
        Button[] buttons = new Button[BreadShape.values().length];
        for (int i = 0; i < buttons.length; i++) {
            BreadShape shape = BreadShape.values()[i];
            buttons[i] = new Button(shape.getDisplayName());
            buttons[i].setOnAction(event -> {
                controller.applyShape(shape);
                onChange.run();
            });
        }
        return buttons;
    }

    private javafx.scene.Node[] createAdditionButtons(StationController controller) {
        return createIngredientButtons(controller, "ADDITION", controller::addAddition);
    }

    private javafx.scene.Node[] createSauceButtons(StationController controller) {
        return createIngredientButtons(controller, "SAUCE", controller::applySauce);
    }

    private javafx.scene.Node[] createIngredientButtons(StationController controller,
                                                        String category,
                                                        Consumer<IngredientType> action) {
        return Arrays.stream(IngredientType.values())
                .filter(type -> category.equals(type.getCategory()))
                .map(type -> {
                    Button button = new Button(type.name());
                    button.setOnAction(event -> {
                        action.accept(type);
                        onChange.run();
                    });
                    return (javafx.scene.Node) button;
                })
                .toArray(javafx.scene.Node[]::new);
    }
}
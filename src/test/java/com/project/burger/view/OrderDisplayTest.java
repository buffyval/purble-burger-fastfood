package com.project.burger.view;

import com.project.burger.model.BreadShape;
import com.project.burger.model.IngredientType;
import com.project.burger.model.Order;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;

public class OrderDisplayTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        OrderDisplayView displayView = new OrderDisplayView();

        // 1. Lista degli ingredienti per il panino CLASSIC di prova
        List<IngredientType> ingredients = List.of(
                IngredientType.LETTUCE,
                IngredientType.ONION,
                IngredientType.TOMATO,
                IngredientType.PICKLES,
                IngredientType.CHEESE
        );

        // 2. Creazione dell'oggetto Order con i 5 parametri richiesti:
        // (BreadShape, List<IngredientType>, boolean targetFries, IngredientType targetSauce, DrinkType targetDrink)
        Order testOrder = new Order(
                BreadShape.CLASSIC,
                ingredients,
                false, // targetFries
                null,  // targetSauce
                null   // targetDrink
        );

        // 3. Renderizziamo l'ordine nel display (punteggio a 5 stelle)
        displayView.renderOrder(testOrder, 1, 5);

        // 4. Layout di test
        StackPane root = new StackPane(displayView);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #2b2b2b;"); // Sfondo scuro di contrasto

        Scene scene = new Scene(root, 400, 400);

        primaryStage.setTitle("Test Visualizzazione OrderDisplayView (Classic)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
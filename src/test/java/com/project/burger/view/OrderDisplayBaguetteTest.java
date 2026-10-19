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

public class OrderDisplayBaguetteTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        OrderDisplayView displayView = new OrderDisplayView();

        // 1. Definiamo la lista degli ingredienti specifici per la Baguette (incluso HAM)
        List<IngredientType> baguetteIngredients = List.of(
                IngredientType.HAM,
                IngredientType.CHEESE,
                IngredientType.PICKLES,
                IngredientType.TOMATO,
                IngredientType.ONION,
                IngredientType.LETTUCE
        );

        // 2. Creiamo l'oggetto Order passando i 5 parametri del costruttore:
        // (BreadShape, List<IngredientType>, boolean targetFries, IngredientType targetSauce, DrinkType targetDrink)
        Order testOrder = new Order(
                BreadShape.BAGUETTE,
                baguetteIngredients,
                false, // targetFries
                null,  // targetSauce
                null   // targetDrink
        );

        // 3. Renderizziamo l'ordine nell'anteprima (punteggio 5 stelle)
        displayView.renderOrder(testOrder, 1, 5);

        // 4. Layout e scena di test
        StackPane root = new StackPane(displayView);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #2b2b2b;"); // Sfondo scuro di contrasto

        Scene scene = new Scene(root, 400, 400);

        primaryStage.setTitle("Test Visualizzazione Baguette con Prosciutto (HAM)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
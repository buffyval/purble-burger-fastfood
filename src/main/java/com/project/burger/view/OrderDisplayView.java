package com.project.burger.view;

import com.project.burger.model.IngredientType;
import com.project.burger.model.Order;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class OrderDisplayView extends VBox {
    private final Label shapeLabel = new Label();
    private final Label ingredientsLabel = new Label();
    private final Label friesLabel = new Label();
    private final Label sauceLabel = new Label();
    private final Label progressLabel = new Label();
    private final Label scoreLabel = new Label();

    public OrderDisplayView() {
        setAlignment(Pos.CENTER);
        setSpacing(6);
        getChildren().addAll(shapeLabel, ingredientsLabel, friesLabel,
                sauceLabel, progressLabel, scoreLabel);
    }

    public void renderOrder(Order order, int currentBurgerCount, int scoreStars) {
        if (order == null) {
            shapeLabel.setText("Shape: -");
            ingredientsLabel.setText("Ingredients: -");
            friesLabel.setText("Fries: -");
            sauceLabel.setText("Sauce: -");
        } else {
            shapeLabel.setText("Shape: " + order.getTargetShape().getDisplayName());
            ingredientsLabel.setText("Ingredients: " + formatIngredients(order));
            friesLabel.setText("Fries: " + (order.isTargetFries() ? "Required" : "Not required"));
            sauceLabel.setText("Sauce: " + formatSauce(order.getTargetSauce()));
        }

        progressLabel.setText("Burgers: " + clamp(currentBurgerCount) + " / 5");
        scoreLabel.setText("Stars: " + stars(scoreStars));
    }

    private String formatIngredients(Order order) {
        return order.getTargetIngredients().stream()
                .map(IngredientType::name)
                .reduce((left, right) -> left + ", " + right)
                .orElse("None");
    }

    private String formatSauce(IngredientType sauce) {
        return sauce == null ? "None" : sauce.name();
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(5, value));
    }

    private String stars(int score) {
        int count = clamp(score);
        return "★".repeat(count) + "☆".repeat(5 - count);
    }
}
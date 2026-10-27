package com.project.burger.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("ClassCanBeRecord") // I prefer to keep the standard class structure
public class Order {
    private final BreadShape targetShape;
    private final List<IngredientType> targetIngredients;
    private final boolean targetFries;
    private final IngredientType targetSauce;
    private final DrinkType targetDrink;

    public Order(BreadShape targetShape, List<IngredientType> targetIngredients,
                 boolean targetFries, IngredientType targetSauce, DrinkType targetDrink) {
        this.targetShape = targetShape;
        this.targetIngredients = new ArrayList<>(targetIngredients);
        this.targetFries = targetFries;
        this.targetSauce = targetSauce;
        this.targetDrink = targetDrink;
    }

    public BreadShape getTargetShape() {
        return targetShape;
    }

    public List<IngredientType> getTargetIngredients() {
        return targetIngredients;
    }

    public boolean isTargetFries() {
        return targetFries;
    }

    public IngredientType getTargetSauce() { return targetSauce; }

    public DrinkType getTargetDrink() {
        return targetDrink;
    }

    public boolean matches(Burger burger) {
        if (burger == null) return false;

        // 1. Calcoliamo gli ingredienti ESATTI che il display ti ha mostrato
        List<IngredientType> expectedIngredients = new ArrayList<>();

        // A. Il display disegna al massimo una volta per tipo queste verdure/formaggi
        List<IngredientType> displayVeggies = List.of(
                IngredientType.LETTUCE, IngredientType.ONION,
                IngredientType.TOMATO, IngredientType.PICKLES, IngredientType.CHEESE
        );
        for (IngredientType type : displayVeggies) {
            if (targetIngredients.contains(type)) {
                expectedIngredients.add(type);
            }
        }

        // B. Il display FORZA sempre e comunque una carne a schermo
        IngredientType expectedMeat = (targetShape == BreadShape.BAGUETTE)
                ? IngredientType.HAM
                : (targetIngredients.contains(IngredientType.HAM) ? IngredientType.HAM : IngredientType.PATTY);

        expectedIngredients.add(expectedMeat);

        // 2. Prendiamo la lista degli ingredienti che tu hai effettivamente messo sul vassoio
        List<IngredientType> actualIngredients = burger.getIngredients().stream()
                .map(Ingredient::getType)
                .toList();

        // 3. Ordiniamo ed eguagliamo
        List<IngredientType> sortedExpected = expectedIngredients.stream().sorted().toList();
        List<IngredientType> sortedActual = actualIngredients.stream().sorted().toList();

        boolean sameIngredients = sortedExpected.equals(sortedActual);

        // 4. Controllo flessibile della salsa:
        // Se l'ordine non richiede salse, il vassoio deve non averne.
        // Se l'ordine richiede una salsa, verifichiamo che sia tra quelle aggiunte dall'utente.
        boolean sauceMatches = (targetSauce == null)
                ? burger.getSauces().isEmpty()
                : burger.getSauces().contains(targetSauce);

        // 5. Restituzione esito
        return Objects.equals(targetShape, burger.getShape())
                && sameIngredients
                && targetFries == burger.isFriesPresent()
                && sauceMatches;
    }

    /**
     * Helper per verificare se due liste contengono gli stessi identici elementi
     * (stessi elementi e stesse quantità), ignorando l'ordine degli elementi.
     */
    private boolean hasSameIngredients(List<IngredientType> list1, List<IngredientType> list2) {
        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false;
        }
        List<IngredientType> sorted1 = list1.stream().sorted().toList();
        List<IngredientType> sorted2 = list2.stream().sorted().toList();
        return sorted1.equals(sorted2);
    }
}

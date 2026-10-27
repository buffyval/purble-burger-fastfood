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

        // 1. Calcoliamo gli ingredienti ESATTI che il display ti ha mostrato (dall'alto verso il basso)
        List<IngredientType> expectedIngredients = new ArrayList<>();

        // A. Verdure/Formaggi nel display
        List<IngredientType> displayVeggies = List.of(
                IngredientType.LETTUCE, IngredientType.ONION,
                IngredientType.TOMATO, IngredientType.PICKLES, IngredientType.CHEESE
        );
        for (IngredientType type : displayVeggies) {
            if (targetIngredients.contains(type)) {
                expectedIngredients.add(type);
            }
        }

        // B. Carne nel display (in coda alla lista visiva)
        IngredientType expectedMeat = (targetShape == BreadShape.BAGUETTE)
                ? IngredientType.HAM
                : (targetIngredients.contains(IngredientType.HAM) ? IngredientType.HAM : IngredientType.PATTY);

        expectedIngredients.add(expectedMeat);

        // 2. Invertiamo la lista del display per ottenere l'ESATTO ordine dal basso verso l'alto (come viene assemblato sul vassoio)
        List<IngredientType> expectedBottomToTop = expectedIngredients.reversed();

        // 3. Prendiamo gli ingredienti che l'utente ha messo sul vassoio (dal basso verso l'alto)
        List<IngredientType> actualIngredients = burger.getIngredients().stream()
                .map(Ingredient::getType)
                .toList();

        // 4. CONFRONTO ESATTO DELLA SEQUENZA VERTICALE
        boolean sameIngredients = expectedBottomToTop.equals(actualIngredients);

        // 5. Controllo flessibile delle salse e delle patatine
        boolean sauceMatches = (targetSauce == null)
                ? burger.getSauces().isEmpty()
                : burger.getSauces().contains(targetSauce);

        // 6. Restituzione esito
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

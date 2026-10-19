package com.project.burger.engine;

import com.project.burger.model.BreadShape;
import com.project.burger.model.IngredientType;
import com.project.burger.model.Order;
import com.project.burger.model.DrinkType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class OrderGenerator {
    public Order generateRandomOrder() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        BreadShape shape = randomEnum(BreadShape.values(), random);
        List<IngredientType> ingredientPool = Arrays.stream(IngredientType.values())
                .filter(type -> !"SAUCE".equals(type.getCategory()) && !"SIDE".equals(type.getCategory()))
                .toList();
        List<IngredientType> ingredients = new ArrayList<>(ingredientPool);
        Collections.shuffle(ingredients, random);
        ingredients = new ArrayList<>(ingredients.subList(0, random.nextInt(1, 5)));

        if (shape == BreadShape.BAGUETTE) {
            for (int i = 0; i < ingredients.size(); i++) {
                if (ingredients.get(i) == IngredientType.PATTY) {
                    ingredients.set(i, IngredientType.HAM);
                }
            }
        }

        boolean fries = random.nextBoolean();
        IngredientType sauce = random.nextBoolean()
                ? Arrays.stream(IngredientType.values())
                .filter(type -> "SAUCE".equals(type.getCategory()))
                .toList()
                .get(random.nextInt(2))
                : null;

        DrinkType drink = random.nextBoolean()
                ? randomEnum(DrinkType.values(), random)
                : null;

        return new Order(shape, ingredients, fries, sauce, drink);
    }

    private <T> T randomEnum(T[] values, ThreadLocalRandom random) {
        return values[random.nextInt(values.length)];
    }
}

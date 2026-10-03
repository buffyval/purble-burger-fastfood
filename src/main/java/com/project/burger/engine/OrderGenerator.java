package com.project.burger.engine;

import com.project.burger.model.BreadShape;
import com.project.burger.model.IngredientType;
import com.project.burger.model.Order;

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

        boolean fries = random.nextBoolean();
        IngredientType sauce = random.nextBoolean()
                ? Arrays.stream(IngredientType.values())
                .filter(type -> "SAUCE".equals(type.getCategory()))
                .toList()
                .get(random.nextInt(3))
                : null;

        return new Order(shape, ingredients, fries, sauce);
    }

    private <T> T randomEnum(T[] values, ThreadLocalRandom random) {
        return values[random.nextInt(values.length)];
    }
}

package com.project.burger.engine;

import com.project.burger.model.Burger;
import com.project.burger.model.Order;

public class GameEngine {
    private static final int MAX_BURGERS = 5;
    private static final int MAX_SCORE_STARS = 5;

    private GameState state;
    private Order currentOrder;
    private final OrderGenerator orderGenerator;
    private int currentBurgerCount;
    private int scoreStars;

    public GameEngine() {
        state = GameState.MENU;
        orderGenerator = new OrderGenerator();
    }

    public void startGame() {
        currentBurgerCount = 1;
        scoreStars = 0;
        state = GameState.PLAYING;
        currentOrder = orderGenerator.generateRandomOrder();
    }

    public void submitBurger(Burger burger) {
        if (currentOrder != null && currentOrder.matches(burger)) {
            if (scoreStars < MAX_SCORE_STARS) { scoreStars++; }
        }

        if (currentBurgerCount >= MAX_BURGERS) {
            currentBurgerCount = MAX_BURGERS; // da togliere?
            state = GameState.GAME_OVER;
        } else {
            currentBurgerCount++;
            currentOrder = orderGenerator.generateRandomOrder();
        }
    }

    @SuppressWarnings("unused") // questo metodo in realtà viene utilizzato
    public GameState getState() {
        return state;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public int getCurrentBurgerCount() {
        return currentBurgerCount;
    }

    public int getScoreStars() {
        return scoreStars;
    }
}

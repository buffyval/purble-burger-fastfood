package com.project.burger.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Locale;

public class ChefView extends StackPane {
    private final ImageView chefSprite;

    public ChefView() {
        chefSprite = new ImageView();
        chefSprite.setFitWidth(180);
        chefSprite.setFitHeight(220);
        chefSprite.setPreserveRatio(true);
        loadSprite("default");
        getChildren().add(chefSprite);
    }

    public void updateChefState(String state) {
        if (state == null || state.isBlank()) {
            loadSprite("default");
            return;
        }

        loadSprite(state.trim().toLowerCase(Locale.ROOT));
    }

    private void loadSprite(String state) {
        URL imageUrl = getClass().getResource("/images/chef_" + state + ".png");
        if (imageUrl != null) {
            chefSprite.setImage(new Image(imageUrl.toExternalForm()));
        } else if (!"default".equals(state)) {
            loadSprite("default");
        }
    }
}

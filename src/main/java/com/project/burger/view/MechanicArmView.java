package com.project.burger.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Locale;

public class MechanicArmView extends StackPane {
    private final ImageView armGraphic;

    public MechanicArmView() {
        armGraphic = new ImageView();
        armGraphic.setFitWidth(180);
        armGraphic.setFitHeight(220);
        armGraphic.setPreserveRatio(true);
        setArmImage("/images/mechanic_arm.png");
        getChildren().add(armGraphic);
    }

    public void lowerArmToPlaceTopper(String shapeName) {
        String normalizedShape = shapeName == null || shapeName.isBlank()
                ? "classic"
                : shapeName.trim().toLowerCase(Locale.ROOT);
        setArmImage("/images/mechanic_arm_place_" + normalizedShape + ".png");
    }

    public void lowerArmToTrash() {
        setArmImage("/images/mechanic_arm_trash.png");
    }

    private void setArmImage(String resourcePath) {
        URL imageUrl = getClass().getResource(resourcePath);
        if (imageUrl != null) {
            armGraphic.setImage(new Image(imageUrl.toExternalForm()));
        }
    }
}

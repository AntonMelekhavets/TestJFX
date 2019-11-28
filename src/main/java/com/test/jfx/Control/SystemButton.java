package com.test.jfx.Control;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;

public class SystemButton extends Button {
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        try {
            this.imageUrl = imageUrl;
            ImageView btnImage = loadImage(imageUrl);
            setBackground(Background.EMPTY);
            setGraphic(btnImage);
            getStyleClass().add("control-button");
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage() + "  Url: " + imageUrl);
        }
    }

    public ImageView loadImage(String url) {
        double IMAGE_HEIGHT = 15;
        double IMAGE_WIDTH = 15;
        Image image = new Image(url, IMAGE_WIDTH, IMAGE_HEIGHT, false, false, false);
        return new ImageView(image);
    }
}

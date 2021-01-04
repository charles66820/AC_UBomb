/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class Sprite {

    public static final int size = 40;
    protected final Pane layer; // Change to protected for bomb explosion
    private ImageView imageView;
    private Image image;

    public Sprite(Pane layer, Image image) {
        this.layer = layer;
        this.image = image;
    }

    public final void setImage(Image image) {
        if (this.image == null || this.image != image) {
            this.image = image;
        }
    }

    public ImageView getImageView() {
        return imageView;
    }

    public abstract void updateImage();

    public abstract Position getPosition();

    public void render() { // Remove final for bomb explosion
        if (imageView != null) {
            remove();
        }
        updateImage();
        imageView = new ImageView(this.image);
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        imageView.setX(getPosition().x * size);
        imageView.setY(getPosition().y * size);
        layer.getChildren().add(imageView);
    }

    public void remove() { // Remove final for bomb explosion
        layer.getChildren().remove(imageView);
        imageView = null;
    }
}

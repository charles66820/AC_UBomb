/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.image;

import fr.ubx.poo.game.Direction;
import javafx.scene.image.Image;

import static fr.ubx.poo.view.image.ImageResource.*;

public final class ImageFactory {
    private final Image[] images;
    private String theme = "default";
    private boolean isPrincess = false;

    private final ImageResource[] princeDirections = new ImageResource[]{
            // Direction { N, E, S, W }
            PRINCE_UP, PRINCE_RIGHT, PRINCE_DOWN, PRINCE_LEFT,
    };

    private final ImageResource[] princessDirections = new ImageResource[]{
            // Direction { N, E, S, W }
            PRINCESS_UP, PRINCESS_RIGHT, PRINCESS_DOWN, PRINCESS_LEFT,
    };

    private final ImageResource[] monstersDirections = new ImageResource[]{
            // Direction { N, E, S, W }
            MONSTER_UP, MONSTER_RIGHT, MONSTER_DOWN, MONSTER_LEFT,
    };

    private final ImageResource[] digits = new ImageResource[]{
            DIGIT_0, DIGIT_1, DIGIT_2, DIGIT_3, DIGIT_4,
            DIGIT_5, DIGIT_6, DIGIT_7, DIGIT_8, DIGIT_9,
    };

    private ImageFactory() {
        images = new Image[ImageResource.values().length];
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static ImageFactory getInstance() {
        return Holder.instance;
    }

    private Image loadImage(String file) {
        return new Image(getClass().getResource("/themes/" + theme + "/images/" + file).toExternalForm());
    }

    public void load() {
        for (ImageResource img : ImageResource.values()) {
            images[img.ordinal()] = loadImage(img.getFileName());
        }
    }

    public Image get(ImageResource img) {
        return images[img.ordinal()];
    }

    public Image getDigit(int i) {
        if (i < 0 || i > 9)
            throw new IllegalArgumentException();
        return get(digits[i]);
    }

    public Image getPlayer(Direction direction) {
        ImageResource[] directions = isPrincess ? princessDirections : princeDirections;
        return get(directions[direction.ordinal()]);
    }

    public Image getTarget() {
        return get(!isPrincess ? PRINCESS : PRINCE);
    }

    public Image getMonsters(Direction direction) {
        return get(monstersDirections[direction.ordinal()]);
    }

    public void setPrincess(boolean isPrincess) {
        this.isPrincess = isPrincess;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * Holder
     */
    private static class Holder {
        /**
         * Instance unique non préinitialisée
         */
        private final static ImageFactory instance = new ImageFactory();
    }

}

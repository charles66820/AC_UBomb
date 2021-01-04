/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

public class SpritePlayer extends SpriteGameObject {
    private ColorAdjust effect = new ColorAdjust();

    public SpritePlayer(Pane layer, Player player) {
        super(layer, null, player);
        updateImage();
    }

    @Override
    public void updateImage() {
        Player player = (Player) go;
        setImage(ImageFactory.getInstance().getPlayer(player.getDirection()));
    }

    @Override
    public void render() {
        super.render();
        Player player = (Player) go;
        if (player.isInvulnerable()) {
            this.effect.setSaturation(-0.3);
            this.effect.setContrast(0.6);
            this.effect.setBrightness(0.3);
            this.getImageView().setEffect(this.effect);
        }
    }
}

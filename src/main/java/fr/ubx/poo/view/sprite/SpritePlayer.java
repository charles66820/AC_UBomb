/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

public class SpritePlayer extends SpriteGameObject {
    private final ColorAdjust effect = new ColorAdjust(); //TODO: changer quand invulnérabilité

    public SpritePlayer(Pane layer, Player player) {
        super(layer, null, player);
        updateImage();
    }

    @Override
    public void updateImage() {
        Player player = (Player) go;
        setImage(ImageFactory.getInstance().getPlayer(player.getDirection()));
    }
}

/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Door;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

import static fr.ubx.poo.view.image.ImageResource.DOOR_CLOSED;
import static fr.ubx.poo.view.image.ImageResource.DOOR_OPENED;

public class SpriteDoor extends SpriteDecor {

    public SpriteDoor(Pane layer, Door door, Position position) {
        super(layer, ImageFactory.getInstance().get(door.isOpen() ? DOOR_OPENED : DOOR_CLOSED), position);
    }
}

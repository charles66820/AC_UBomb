package fr.ubx.poo.sprite;

import fr.ubx.poo.engine.Position;
import fr.ubx.poo.entity.go.GameObject;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public abstract class SpriteGameObject extends Sprite {
    protected final GameObject go;

    public SpriteGameObject(Pane layer, Image image, GameObject go) {
        super(layer, image);
        this.go = go;
    }

    @Override
    public Position getPosition() {
        return go.getPosition();
    }
}

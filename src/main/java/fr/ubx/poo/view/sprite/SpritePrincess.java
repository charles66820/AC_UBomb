package fr.ubx.poo.view.sprite;

import static fr.ubx.poo.view.image.ImageResource.*;

import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Princess;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpritePrincess extends SpriteGameObject {

    public SpritePrincess(Pane layer, Princess princess) {
        super(layer, null, princess);
        updateImage();
    }

    @Override
    public void updateImage() {
        setImage(ImageFactory.getInstance().get(PRINCESS));
    }
}

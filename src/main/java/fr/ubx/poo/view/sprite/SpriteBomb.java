package fr.ubx.poo.view.sprite;

import static fr.ubx.poo.view.image.ImageResource.*;

import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.item.Bomb;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import static fr.ubx.poo.view.image.ImageResource.PRINCESS;

public class SpriteBomb extends SpriteGameObject {

    public SpriteBomb(Pane layer, Image image, Bomb bomb) {
        super(layer, null, bomb);
        updateImage();
    }

    @Override
    //TODO : update l'image de la bombe à chaque secondes avant qu'elle explose (avec un timer tout ça ...)
    public void updateImage() {
        setImage(ImageFactory.getInstance().get(BOMB_1));
    }
}

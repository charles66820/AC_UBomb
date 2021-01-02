package fr.ubx.poo.view.sprite;

import static fr.ubx.poo.view.image.ImageResource.*;

import fr.ubx.poo.model.go.item.Bomb;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

public class SpriteBomb extends SpriteGameObject {

    public SpriteBomb(Pane layer, Bomb bomb) {
        super(layer, null, bomb);
        updateImage();
    }

    @Override
    public void updateImage() {
        Bomb b = (Bomb) this.go;
        if ((b.getTimer() * 100) / b.getExplosionCooldown() <= 250) {
            setImage(ImageFactory.getInstance().get(BOMB_4));
        } else if ((b.getTimer() * 100) / b.getExplosionCooldown() <= 500) {
            setImage(ImageFactory.getInstance().get(BOMB_3));
        } else if ((b.getTimer() * 100) / b.getExplosionCooldown() <= 750) {
            setImage(ImageFactory.getInstance().get(BOMB_2));
        } else if ((b.getTimer() * 100) / b.getExplosionCooldown() <= 1000) {
            setImage(ImageFactory.getInstance().get(BOMB_1));
        } else {
            setImage(ImageFactory.getInstance().get(EXPLOSION));
        }
    }
}

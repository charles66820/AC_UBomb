package fr.ubx.poo.view.sprite;

import static fr.ubx.poo.view.image.ImageResource.*;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.item.Bomb;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class SpriteBomb extends SpriteGameObject {

    List<ImageView> explosionImageViews = new ArrayList<>();

    public SpriteBomb(Pane layer, Bomb bomb) {
        super(layer, null, bomb);
        updateImage();
    }

    @Override
    public void updateImage() {
        Bomb b = (Bomb) this.go;
        if ((b.getTimer() * 100) / b.getExplosionCooldown() <= 25) { // 25%
            setImage(ImageFactory.getInstance().get(BOMB_4));
        } else if ((b.getTimer() * 100) / b.getExplosionCooldown() <= 50) { // 50%
            setImage(ImageFactory.getInstance().get(BOMB_3));
        } else if ((b.getTimer() * 100) / b.getExplosionCooldown() <= 75) { // 75%
            setImage(ImageFactory.getInstance().get(BOMB_2));
        } else if ((b.getTimer() * 100) / b.getExplosionCooldown() <= 100) { // 100%
            setImage(ImageFactory.getInstance().get(BOMB_1));
        } else {
            setImage(ImageFactory.getInstance().get(EXPLOSION));
        }
    }

    @Override
    public void render() {
        super.render();
        for (Position explosionPosition : ((Bomb) go).getExplosionPositions()) {
            ImageView imageView = new ImageView(ImageFactory.getInstance().get(EXPLOSION));
            imageView.setX(explosionPosition.x * size);
            imageView.setY(explosionPosition.y * size);
            explosionImageViews.add(imageView);
            layer.getChildren().add(imageView);
        }
    }

    @Override
    public final void remove() {
        super.remove();
        for (ImageView explosionImageView : explosionImageViews)
            layer.getChildren().remove(explosionImageView);
        explosionImageViews.clear();
    }
}

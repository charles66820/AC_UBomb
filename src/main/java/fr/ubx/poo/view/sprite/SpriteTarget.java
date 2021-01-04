package fr.ubx.poo.view.sprite;

import static fr.ubx.poo.view.image.ImageResource.*;

import fr.ubx.poo.model.go.character.Target;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

public class SpriteTarget extends SpriteGameObject {

    public SpriteTarget(Pane layer, Target target) {
        super(layer, null, target);
        updateImage();
    }

    @Override
    public void updateImage() {
        setImage(ImageFactory.getInstance().getTarget());
    }
}

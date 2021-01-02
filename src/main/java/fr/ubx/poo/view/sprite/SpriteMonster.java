package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

public class SpriteMonster extends SpriteGameObject {

    public SpriteMonster(Pane layer, Monster monster) {
        super(layer, null, monster);
        updateImage();
    }

    @Override
    public void updateImage() {
        Monster monster = (Monster) go;
        setImage(ImageFactory.getInstance().getMonsters(monster.getDirection()));
    }
}

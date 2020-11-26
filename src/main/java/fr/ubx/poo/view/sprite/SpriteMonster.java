package fr.ubx.poo.view.sprite;

import static fr.ubx.poo.view.image.ImageResource.*;

import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteMonster extends SpriteGameObject {

    public SpriteMonster(Pane layer, Monster monster) {
        super(layer, null, monster);
        updateImage();
    }

    @Override
    //TODO : update l'image du monstre en fonction de sa direction
    public void updateImage() {
        setImage(ImageFactory.getInstance().get(MONSTER_DOWN));
    }
}

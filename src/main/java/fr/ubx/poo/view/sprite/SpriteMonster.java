package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class SpriteMonster extends SpriteGameObject {

    List<Circle> circlePath; // For debug path finding

    public SpriteMonster(Pane layer, Monster monster) {
        super(layer, null, monster);
        if (Monster.DEBUG_PATHFINDING) this.circlePath = new ArrayList<>();
        updateImage();
    }

    @Override
    public void updateImage() {
        Monster monster = (Monster) go;
        setImage(ImageFactory.getInstance().getMonsters(monster.getDirection()));

        if (Monster.DEBUG_PATHFINDING) {
            List<Position> path = monster.getPath();
            if (path != null) {
                for (Position p : path) {
                    Circle c = new Circle((p.x * size) + 20, (p.y * size) + 20, 20, Color.BLUE);
                    this.circlePath.add(c);
                    layer.getChildren().add(c);
                }
            }
        }
    }

    @Override
    public final void remove() {
        super.remove();
        if (Monster.DEBUG_PATHFINDING && circlePath != null) {
            for (Circle c : circlePath)
                layer.getChildren().remove(c);
            circlePath.clear();
        }
    }
}

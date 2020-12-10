package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;

public class Monster extends Character implements Movable {

    public Monster(Game game, Position position) {
        super(game, position);
    }

    public void update(long now) {
        // On move
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);

                // Hit the player if is on same position
                Player player = this.game.getPlayer();
                if (this.getPosition().equals(player.getPosition())) {
                    player.setLives(player.getLives() - 1);
                }
            }
            moveRequested = false;
        }

    }

    //TODO : rajouter getDirection, requestMove, update.

}

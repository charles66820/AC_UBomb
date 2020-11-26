package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;

public class Monster extends GameObject implements Movable {

    public Monster(Game game, Position position) {
        super(game, position);
    }

    @Override
    public boolean canMove(Direction direction) {
        return false;
    }

    @Override
    public void doMove(Direction direction){}

    //TODO : rajouter getDirection, requestMove, update.

}

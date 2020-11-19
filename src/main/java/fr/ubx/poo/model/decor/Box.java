package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.model.Movable;

public class Box extends Decor implements Movable {

    public String toString() {
        return "Box";
    }

    @Override
    public boolean canMove(Direction direction) {
        return false;
    }

    @Override
    public void doMove(Direction direction) {}
}

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;

public abstract class Character extends GameObject implements Movable {
    protected boolean alive = true;
    Direction direction; // Player looks at the direction
    protected boolean moveRequested = false;

    public Character(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
    }

    public void update(long now) {
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor decor = this.game.getCurentWorld().get(nextPos);
        return (this.game.getCurentWorld().isInside(nextPos)) && ((decor == null) || (decor.isTraversable()));
    }

    @Override
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    public boolean isAlive() {
        return alive;
    }

    public Direction getDirection() {
        return direction;
    }

}

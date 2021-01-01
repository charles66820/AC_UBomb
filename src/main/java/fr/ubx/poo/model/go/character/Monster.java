package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Door;

public class Monster extends Character implements Movable {

    private final long moveFrequency; // In ns
    private long lastCallTime;

    public Monster(Game game, Position position, int moveFrequencyInMs) {
        super(game, position);
        this.moveFrequency = 1000000L * moveFrequencyInMs;
    }

    public void update(long now) {
        // Init lasteCallTime
        if (this.lastCallTime == 0) this.lastCallTime = now;

        // Request monste move after a moment
        if ((now - lastCallTime) >= moveFrequency) {
            this.randomAI(); // TODO: smartAI()
            this.lastCallTime = now;
        }

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

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor decor = this.game.getWorld().get(nextPos);
        // Collition with monster
        for (Monster monster : this.game.getWorld().getMonsters()) if (nextPos.equals(monster.getPosition())) return false;
        return (this.game.getWorld().isInside(nextPos)) && ((decor == null) || (decor.isTraversable() && !(decor instanceof Door)));
    }

    public void randomAI() {
        this.requestMove(Direction.random());
    }

    public void smartAI() {

    }

    public void die() {
        this.alive = false;
    }

}

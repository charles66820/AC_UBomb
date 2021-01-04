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

    public Monster(Game game, Position position, double moveFrequencyInMs) {
        super(game, position);
        this.moveFrequency = (long) (1000000L * moveFrequencyInMs);
    }

    public void update(long now) {
        // Init lastCallTime
        if (this.lastCallTime == 0) this.lastCallTime = now;

        // Request monster move after a moment
        if ((now - lastCallTime) >= moveFrequency) {
            if (this.game.isSmartAI()) this.smartAI();
            else this.randomAI();
            this.lastCallTime = now;
        }

        // On move
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);

                // Hit the player if is on same position
                Player player = this.game.getPlayer();
                if (this.getPosition().equals(player.getPosition())) {
                    player.removeLives(1);
                    this.game.getPlayer().setInvulnerable(true);
                    this.game.getPlayer().setLastTimeInvulnerable(now);
                }
            }
            moveRequested = false;
        }

    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor decor = this.game.getWorld().get(nextPos);
        // Collision with monster
        for (Monster monster : this.game.getWorld().getMonsters())
            if (nextPos.equals(monster.getPosition())) return false;
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

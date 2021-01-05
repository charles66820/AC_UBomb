package fr.ubx.poo.model.go.item;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.collectable.Collectable;
import fr.ubx.poo.model.decor.collectable.Key;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Monster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bomb extends GameObject {
    private final int rangeMax;
    private long initTimer; // time when bomb was created
    private long timer; // current time before explosion
    private final long explosionCooldown; // total time before explosion
    private boolean isExploded; // state of the bomb if it is exploded or not
    private boolean canBeRemove; // state of the bomb if it can be remove
    List<Position> explosionPositions = new ArrayList<>();

    public Bomb(Game game, Position position, long initTimer) {
        super(game, position);
        this.initTimer = initTimer;
        this.isExploded = false;
        this.canBeRemove = false;
        this.explosionCooldown = (long) (1000000L * this.game.getExplosionCooldown());
        this.rangeMax = this.game.getPlayer().getRangebomb();
    }

    public void update(long now) {
        this.timer = now - this.getInitTimer();
        if (this.getTimer() > this.getExplosionCooldown() && !this.isExploded) {
            this.explosion();
        }

        if (this.getTimer() > this.getExplosionCooldown() + (1000000L * this.game.getExplosionDuration()) && this.isExploded) {
            this.canBeRemove = true;
        }
    }

    public void explosion() { //TODO: les décors arrêtent l'explosion et une seule box peut être détruite par direction !
        explosionSpread(this.getPosition());
        for (int i = 0; i <= 3; i++) {
            Direction direction = Direction.values()[i]; // select each direction
            for (int j = 1; j <= rangeMax; j++) {
                Position nextPos = direction.nextPosition(this.getPosition(), j);
                if (explosionSpread(nextPos)) break;
            }
            this.isExploded = true;
        }
    }

    /**
     * @param pos
     * @return stopPropagation
     */
    private boolean explosionSpread(Position pos) {
        //if it is a decor
        Decor decor = this.game.getWorld().get(pos);
        if (decor != null) {
            if (decor instanceof Box) {
                this.game.getWorld().clear(pos);
                explosionPositions.add(pos); // To show explosion
                return true;
            } else if (decor instanceof Collectable && !(decor instanceof Key)) {
                this.game.getWorld().clear(pos);
                explosionPositions.add(pos); // To show explosion
            } else if (decor.isExplosionStop()) {
                return true;
            }
        } else {
            explosionPositions.add(pos); // To show explosion
        }
        //if explosion hit the player
        if (pos.equals(this.game.getPlayer().getPosition())) {
            this.game.getPlayer().removeLives(1);
            this.game.getPlayer().setInvulnerable(true);
            this.game.getPlayer().setLastTimeInvulnerable(this.timer);
        }
        //if explosion hit a monster
        Collection<Monster> monsters = this.game.getWorld().getMonsters();
        for (Monster monster : monsters) {
            if (pos.equals(monster.getPosition())) {
                monster.die(); // kill monster
            }
        }
        //if explosion hit another bomb
        Collection<Bomb> bombs = this.game.getWorld().getBombs();
        for (Bomb bomb : bombs) {
            if (pos.equals(bomb.getPosition())) {
                bomb.initTimer = this.initTimer;
            }
        }
        return false;
    }

    public long getInitTimer() {
        return initTimer;
    }

    public long getTimer() {
        return timer;
    }

    public long getExplosionCooldown() {
        return explosionCooldown;
    }

    public boolean canBeRemove() {
        return canBeRemove;
    }

    public List<Position> getExplosionPositions() {
        return explosionPositions;
    }

}

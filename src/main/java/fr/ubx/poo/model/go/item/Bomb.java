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

import java.util.Collection;

public class Bomb extends GameObject {
    private final long initTimer; // time when bomb was created
    private long timer; // current time before explosion
    private final long explosionCooldown; // total time before explosion
    private boolean isExploded; // state of the bomb if it is exploded or not

    public long getInitTimer() {
        return initTimer;
    }

    public long getTimer() {
        return timer;
    }

    public long getExplosionCooldown() {
        return explosionCooldown;
    }

    public boolean isExploded() {
        return isExploded;
    }


    public Bomb(Game game, Position position, long initTimer) {
        super(game, position);
        this.initTimer = initTimer;
        this.isExploded = false;
        this.explosionCooldown = 100000L * this.game.getExplosionCooldown();
    }

    public void update(long now) {
        this.timer = now - this.getInitTimer();
        if ((this.getTimer() * 100) / this.getExplosionCooldown() > 1000 + this.game.getExplosionDuration()) {
            this.explosion();
        }
    }

    public void explosion() {
        int rangeMax = this.game.getPlayer().getRangebomb();
        for (int i = 0; i <= 3; i++) {
            Direction direction = Direction.values()[i]; // select each direction
            for (int j = 1; j <= rangeMax; j++) {
                Position nextPos = direction.nextPosition(this.getPosition(), j);
                //if it is a decor
                Decor decor = this.game.getWorld().get(nextPos);
                if (decor != null) {
                    if (decor instanceof Box) {
                        this.game.getWorld().clear(nextPos);
                        break;
                    }
                    else if (decor instanceof Collectable && !(decor instanceof Key)) {
                        this.game.getWorld().clear(nextPos);
                    }
                    else if (decor.isExplosionStop()) {
                        break;
                    }
                }
                //if explosion hit the player
                if (nextPos.equals(this.game.getPlayer().getPosition())) {
                    this.game.getPlayer().removeLives(1);
                }
                //if explosion hit a monster
                Collection<Monster> monsters = this.game.getWorld().getMonsters();
                for (Monster monster : monsters) {
                    if (nextPos.equals(monster.getPosition())) {
                        monster.die(); // kill monster
                    }
                }
            }
        }
        this.isExploded = true;
    }
}

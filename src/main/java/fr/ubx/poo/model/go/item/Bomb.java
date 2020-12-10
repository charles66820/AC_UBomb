package fr.ubx.poo.model.go.item;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.GameObject;

public class Bomb extends GameObject {
    private long initTimer;
    private long timer;
    private final long explosionCooldown = 400000000;

    public long getInitTimer() {
        return initTimer;
    }

    public long getTimer() {
        return timer;
    }

    public long getExplosionCooldown() {
        return explosionCooldown;
    }

    public Bomb(Game game, Position position, long initTimer) {
        super(game, position);
        this.initTimer = initTimer;
    }

    public void update(long now) {
        this.timer = now - this.getInitTimer();

    }
}

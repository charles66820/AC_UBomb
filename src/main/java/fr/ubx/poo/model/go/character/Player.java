/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.item.Bomb;

import java.util.Collection;

public class Player extends Character implements Movable {
    private boolean winner;
    private boolean invulnerable;
    private long lastTimeInvulnerable;
    // Stats
    private int lives;
    private int bomb = 1;
    private int rangebomb = 1;
    private int bombMax = 1;
    private int key = 0;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
        this.invulnerable = false;
        this.lastTimeInvulnerable = 0;
    }

    @Override
    public void update(long now) {
        // Check if player die
        if (this.getLives() <= 0) {
            this.alive = false;
        }

        //Check if player is still invulnerable
        if (now >= this.lastTimeInvulnerable + 1000000000L) {
            this.invulnerable = false;
        }

        // On move
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);

                Position newPos = this.getPosition();
                // If a monster is on player position player is hit
                Collection<Monster> monsters = this.game.getWorld().getMonsters();
                for (Monster monster : monsters) {
                    if (newPos.equals(monster.getPosition())) {
                        removeLives(1);
                        this.invulnerable = true;
                        this.lastTimeInvulnerable = now;
                    }
                }

                Decor decor = this.game.getWorld().get(newPos);
                if (decor != null) {
                    decor.takenBy(this);
                }
            }
        }
        moveRequested = false;

        // Check is player win
        Position pos = this.getPosition();
        Target target = this.game.getTarget();
        if (target != null && pos.equals(target.getPosition())) {
            this.winner = true;
        }
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor decor = this.game.getWorld().get(nextPos);
        if (decor instanceof Box) {
            Position nextBoxPos = direction.nextPosition(nextPos);
            if (!this.game.getWorld().isInside(nextBoxPos)) return false; // Fix bug with box move out of world border
            Decor d = this.game.getWorld().get(nextBoxPos);
            if (d != null) return false;

            // Collision with monster
            for (Monster monster : this.game.getWorld().getMonsters())
                if (nextBoxPos.equals(monster.getPosition())) return false;
            // Collision with bombs
            for (Bomb bomb : this.game.getWorld().getBombs()) {
                if (nextBoxPos.equals(bomb.getPosition())) {
                    return false;
                }
            }

            this.game.getWorld().clear(nextPos);
            this.game.getWorld().set(nextBoxPos, decor);
            return true;
        }
        return (this.game.getWorld().isInside(nextPos)) && ((decor == null) || (decor.isTraversable()));
    }

    public boolean isWinner() {
        return this.winner;
    }

    public boolean isInvulnerable() {
        return this.invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    public void setLastTimeInvulnerable(long lastTimeInvulnerable) {
        this.lastTimeInvulnerable = lastTimeInvulnerable;
    }

    public void addLives(int pv) {
        this.lives = this.lives + pv;
    }

    public void removeLives(int pv) {
        if (!this.invulnerable) {
            this.lives = this.lives - pv;
        }
    }

    // Stats
    public int getLives() {
        return lives;
    }

    public int getBomb() {
        return bomb;
    }

    public void setBomb(int bomb) {
        this.bomb = bomb;
    }

    public int getRangebomb() {
        return rangebomb;
    }

    public void setRangebomb(int rangebomb) {
        this.rangebomb = rangebomb;
    }

    public int getBombMax() {
        return bombMax;
    }

    public void setBombMax(int bombMax) {
        this.bombMax = bombMax;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Game getGame() {
        return this.game;
    }

}

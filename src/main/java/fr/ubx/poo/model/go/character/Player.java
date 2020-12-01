/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.collectable.*;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;

import java.util.Collection;

public class Player extends GameObject implements Movable {

    // TODO : supprimer le final
    private final boolean alive = true;
    private boolean winner;
    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    private int bomb = 1;
    private int rangebomb = 1;
    private int key = 0;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
    }

    public int getLives() {
        return lives;
    }

    public int getBomb() {
        return bomb;
    }

    public int getRangebomb() {
        return rangebomb;
    }

    public int getKey() {
        return key;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setBomb(int bomb) {
        this.bomb = bomb;
    }

    public void setRangebomb(int rangebomb) {
        this.rangebomb = rangebomb;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor decor = this.game.getWorld().get(nextPos);
        return (this.game.getWorld().isInside(nextPos)) && ((decor == null) || (decor.isTraversable()));
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
                //TODO : remove all sprite of collectables
                Position pos = this.getPosition();
                Decor decor = this.game.getWorld().get(pos);
                Collection<Monster> monsters = this.game.getMonsters();
                //Lives
                for (Monster monster : monsters) {
                    if(pos.equals(monster.getPosition())){
                        setLives(this.getLives()-1);
                    }
                }
                if (decor instanceof Heart){
                    setLives(this.getLives()+1);
                    this.game.getWorld().clear(pos);
                }
                //TODO: loose lives (explosions)
                //Keys
                else if (decor instanceof Key){
                    setKey(this.getKey()+1);
                    this.game.getWorld().clear(pos);
                }
                //Bomb increased
                else if (decor instanceof BombNumberInc){
                    setBomb(this.getBomb()+1);
                    this.game.getWorld().clear(pos);
                }
                //Bomb deceased
                else if (decor instanceof BombNumberDec){
                    if (this.getBomb() > 1){
                        setBomb(this.getBomb()-1);
                        this.game.getWorld().clear(pos);
                    }
                }
                //Range bomb increased
                else if (decor instanceof BombRangeInc){
                    setRangebomb(this.getRangebomb()+1);
                    this.game.getWorld().clear(pos);
                }
                //Range bomb decreased
                else if (decor instanceof BombRangeDec){
                    if(this.getRangebomb() > 1) {
                        setRangebomb(this.getRangebomb()-1);
                        this.game.getWorld().clear(pos);
                    }
                }
            }
        }
        moveRequested = false;
    }

    public boolean isWinner() {
        Position pos = this.getPosition();
        Princess princess = this.game.getPrincess();
        if (pos.equals(princess.getPosition())){
            return winner = true;
        }
        return winner = false;
    }

    public boolean isAlive() {
        if (this.getLives() <= 0){
            return !alive;
        }
        return alive;
    }

}

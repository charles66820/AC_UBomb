/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.item.Bomb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static fr.ubx.poo.game.WorldEntity.*;

public class World {
    private final Map<Position, Decor> grid; // World grid with all decors and collectable (For collision)
    private final WorldEntity[][] raw; // World grid with all entities
    public final Dimension dimension;
    private boolean changed = true;
    private final List<Monster> monsters = new ArrayList<>();
    private final List<Bomb> bombs = new ArrayList<>();

    public World(WorldEntity[][] raw) {
        this.raw = raw;
        dimension = new Dimension(raw.length, raw[0].length);
        grid = WorldBuilder.build(raw, dimension);
    }

    public Position findPlayer() throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == Player) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Player");
    }

    public Position findTarget() throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == Target) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Target");
    }

    public Collection<Position> getMonsterPositions() {
        Collection<Position> pos = new ArrayList<Position>();
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == Monster) {
                    pos.add(new Position(x, y));
                }
            }
        }
        return pos;
    }

    public Decor get(Position position) {
        return grid.get(position);
    }

    public void set(Position position, Decor decor) {
        grid.put(position, decor);
    }

    public void clear(Position position) {
        grid.remove(position);
        this.changed = true;
    }

    public void forEach(BiConsumer<Position, Decor> fn) {
        grid.forEach(fn);
    }

    public Collection<Decor> values() {
        return grid.values();
    }

    public boolean isInside(Position position) {
        return position.x >= 0 && position.x < this.dimension.width &&
                position.y >= 0 && position.y < this.dimension.height;
    }

    public boolean isEmpty(Position position) {
        return grid.get(position) == null;
    }

    public boolean hasChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public Collection<Monster> getMonsters() {
        return monsters;
    }

}

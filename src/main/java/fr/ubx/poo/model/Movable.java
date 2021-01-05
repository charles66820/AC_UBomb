/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model;

import fr.ubx.poo.game.Direction;

public interface Movable {
    boolean canMove(Direction direction);

    void doMove(Direction direction);
}

/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.go.character.Player;

/***
 * A decor is an element that does not know its own position in the grid.
 */
public class Decor extends Entity {
    public boolean isTraversable(){
        return false;
    }
    public void takenBy(Player player) { }
    public boolean isExplosionStop() { return true;} // Check if explosion continue at the same direction
}

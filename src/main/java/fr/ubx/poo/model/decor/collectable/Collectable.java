package fr.ubx.poo.model.decor.collectable;

import fr.ubx.poo.model.decor.Decor;

public abstract class Collectable extends Decor {
    @Override
    public boolean isTraversable() {
        return true;
    }

    @Override
    public boolean isExplosionStop() {
        return false;
    }
}

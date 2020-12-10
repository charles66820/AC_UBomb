package fr.ubx.poo.model.decor.collectable;

import fr.ubx.poo.model.go.character.Player;

public class BombNumberInc extends Collectable {
    @Override
    public String toString() {
        return "BombNumberInc";
    }

    @Override
    public void takenBy(Player player) {
        player.setBomb(player.getBomb() + 1);
        player.getGame().getWorld().clear(player.getPosition());
    }
}

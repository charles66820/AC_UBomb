package fr.ubx.poo.model.decor.collectable;

import fr.ubx.poo.model.go.character.Player;

public class BombRangeInc extends Collectable {
    @Override
    public String toString() {
        return "BombRangeInc";
    }

    @Override
    public void takenBy(Player player) {
        player.setRangebomb(player.getRangebomb() + 1);
        player.getGame().getWorld().clear(player.getPosition());
    }
}

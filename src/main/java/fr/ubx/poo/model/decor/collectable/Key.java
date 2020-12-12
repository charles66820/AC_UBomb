package fr.ubx.poo.model.decor.collectable;

import fr.ubx.poo.model.go.character.Player;

public class Key extends Collectable {
    @Override
    public String toString() {
        return "Key";
    }

    @Override
    public void takenBy(Player player) {
        // TODO: re fix key bug add if player have key
        player.setKey(player.getKey() + 1);
        player.getGame().getWorld().clear(player.getPosition());
    }
}

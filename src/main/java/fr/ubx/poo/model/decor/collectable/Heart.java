package fr.ubx.poo.model.decor.collectable;

import fr.ubx.poo.model.go.character.Player;

public class Heart extends Collectable {
    @Override
    public String toString() {
        return "Heart";
    }

    @Override
    public void takenBy(Player player) {
        player.addLives(1);
        player.getGame().getCurentWorld().clear(player.getPosition());
    }
}

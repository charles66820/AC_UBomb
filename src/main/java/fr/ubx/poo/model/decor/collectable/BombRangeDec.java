package fr.ubx.poo.model.decor.collectable;

import fr.ubx.poo.model.go.character.Player;

public class BombRangeDec extends Collectable {
    @Override
    public String toString() {
        return "BombRangeDec";
    }

    @Override
    public void takenBy(Player player) {
        if (player.getRangebomb() > 1) {
            player.setRangebomb(player.getRangebomb() - 1);
            player.getGame().getCurentWorld().clear(player.getPosition());
        }
    }
}

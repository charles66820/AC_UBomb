package fr.ubx.poo.model.decor.collectable;


import fr.ubx.poo.model.go.character.Player;

public class BombNumberDec extends Collectable {
    @Override
    public String toString() {
        return "BombNumberDec";
    }

    @Override
    public void takenBy(Player player) {
        if (player.getBombMax() > 1) {
            player.setBomb(player.getBomb() - 1);
            player.setBombMax(player.getBombMax() - 1);
            player.getGame().getWorld().clear(player.getPosition());
        }
    }
}

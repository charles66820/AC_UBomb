package fr.ubx.poo.model.decor.collectable;


import fr.ubx.poo.model.go.character.Player;

public class BombNumberDec extends Collectable {
    @Override
    public String toString() {
        return "BombNumberDec";
    }

    @Override
    public void takenBy(Player player) {
        //TODO: prendre en compte le malus dans TOUTES les situations possibles
        if (player.getBomb() > 1) {
            player.setBomb(player.getBomb() - 1);
            player.getGame().getWorld().clear(player.getPosition());
        }
    }
}

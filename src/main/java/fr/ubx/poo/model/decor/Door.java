package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;

public class Door extends Decor {
    // TODO: gérer la destination de la porte
    private boolean isOpen;
    private boolean isNext;

    public Door(boolean isOpen, boolean isNext) {
        this.isOpen = isOpen;
        this.isNext = isNext;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isNext() {
        return isNext;
    }

    public void take(Player player, Position pos) {
        player.takeDoor(this);
    }

    @Override
    public String toString() {
        return "Door";
    }

    @Override
    public boolean isTraversable() {
        return isOpen;
    }
}

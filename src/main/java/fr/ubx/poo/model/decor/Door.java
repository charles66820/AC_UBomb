package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Door extends Decor {
    private boolean isOpen;
    private final boolean isNext;

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

    public void takenBy(Player player) {
        if (this.isNext)
            player.getGame().goNextWord();
        else
            player.getGame().goPrevWord();
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

package fr.ubx.poo.model.decor;

public class Door extends Decor {
    boolean isOpen;

    public Door(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    @Override
    public String toString() {
        return "Door";
    }
}

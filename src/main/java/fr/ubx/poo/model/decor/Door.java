package fr.ubx.poo.model.decor;

public class Door extends Decor {
    //TODO: gérer la destination de la porte
    private boolean isOpen;

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

    @Override
    public boolean isTraversable() {
        return isOpen;
    }
}

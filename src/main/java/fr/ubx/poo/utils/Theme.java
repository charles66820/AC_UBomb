package fr.ubx.poo.utils;

import java.util.Arrays;

public enum Theme {
    DEFAULT("default", "Default"),
    MIRACULOUS("miraculous", "Miraculous ladybug");

    public final String name;
    public final String title;

    Theme(String name, String title) {
        this.name = name;
        this.title = title;
    }

    public static Theme get(String name) {
        return Arrays.stream(values())
                .filter(e -> e.name.equals(name))
                .findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return this.title;
    }
}
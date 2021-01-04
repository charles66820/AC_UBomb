package fr.ubx.poo.utils;

import java.util.Arrays;

public enum Lang {
    ENGLISH("en", "English"),
    FRENCH("fr", "Français");

    public final String code;
    public final String title;

    Lang(String name, String title) {
        this.code = name;
        this.title = title;
    }

    public static Lang get(String code) {
        return Arrays.stream(values())
                .filter(e -> e.code.equals(code))
                .findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return this.title;
    }
}

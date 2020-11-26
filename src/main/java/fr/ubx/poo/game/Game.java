/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.character.Princess;

public class Game {

    private final World world;
    private final Player player;
    private final Princess princesss;
    private final String worldPath;
    public int initPlayerLives;

    public Game(String worldPath) {
        world = new WorldStatic();
        this.worldPath = worldPath;
        loadConfig(worldPath);
        Position positionPlayer = null;
        try {
            positionPlayer = world.findPlayer();
            player = new Player(this, positionPlayer);
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

        Position positionPrincess = null;
        Princess tmpPrincess;
        try {
            positionPrincess = world.findPrincess();
            tmpPrincess = new Princess(this, positionPrincess);
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            tmpPrincess = null;
        }
        princesss = tmpPrincess;
    }

    public int getInitPlayerLives() {
        return initPlayerLives;
    }

    private void loadConfig(String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "3"));
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Princess getPincess() {
        return this.princesss;
    }
}

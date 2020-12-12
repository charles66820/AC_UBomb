/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.*;
import java.util.*;

import fr.ubx.poo.model.decor.Door;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.character.Princess;

public class Game {

    private World currentWorld;
    private final List<World> worlds;
    private final Player player;
    private final String worldPath;
    private String worldPrefix;
    private String extension;
    private int nbLevels;
    private int currentLevel = 0;
    public int initPlayerLives;
    private boolean worldChanged = false;

    public Game(String worldPath) {
        this.worldPath = worldPath;
        this.worlds = new ArrayList<>();
        loadConfig(worldPath);
        currentWorld = worlds.get(0);

        Position positionPlayer = null;
        try {
            positionPlayer = currentWorld.findPlayer();
            player = new Player(this, positionPlayer);
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    private void loadConfig(String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "3"));
            this.worldPrefix = prop.getProperty("prefix", "level");
            this.extension = prop.getProperty("extension", ".txt");
            this.nbLevels = Integer.parseInt(prop.getProperty("levels", "0"));
            if (nbLevels == 0) {
                // Load static world for demo on levels is define to 0
                this.worlds.add(new WorldStatic());
            } else {
                for (int i = 1; i < nbLevels + 1; i++)
                    loadWorld(i);
            }
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    private void loadWorld(int levelNum) throws IOException {
        InputStream input = new FileInputStream(new File(this.worldPath, this.worldPrefix + levelNum + this.extension));

        ArrayList<ArrayList<WorldEntity>> worldArray = new ArrayList<>();
        ArrayList<WorldEntity> worldArrayRow = new ArrayList<>();
        int c;
        int rowLength = 0;
        do {
            c = input.read();
            if (c == '\n') {
                worldArray.add(worldArrayRow);
                rowLength = worldArrayRow.size();
                worldArrayRow = new ArrayList<>();
            } else {
                Optional<WorldEntity> op = WorldEntity.fromCode((char) c);
                if (op.isPresent()) worldArrayRow.add(op.get());
            }
        } while (c != -1);

        WorldEntity[][] worldE = new WorldEntity[worldArray.size()][rowLength];
        for (int i = 0; i < worldArray.size(); i++)
            for (int j = 0; j < rowLength; j++)
                worldE[i][j] = worldArray.get(i).get(j);

        World world = new World(worldE);
        for (Position pos : world.getMonsterPositions()) world.getMonsters().add(new Monster(this, pos));
        this.worlds.add(world);
    }

    public void goNextWord() {
        this.worldChanged = true;
        if (currentLevel < nbLevels) {
            this.currentWorld = this.worlds.get(++currentLevel);
            this.currentWorld.forEach((p, d) -> {
                if (d instanceof Door && !((Door) d).isNext()) player.setPosition(p);
            });
        }
    }

    public void goPrevWord() {
        this.worldChanged = true;
        if (currentLevel > 0) {
            this.currentWorld = this.worlds.get(--currentLevel);
            this.currentWorld.forEach((p, d) -> {
                if (d instanceof Door && ((Door) d).isNext()) player.setPosition(p);
            });
        }
    }

    public World getWorld() {
        return currentWorld;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getInitPlayerLives() {
        return initPlayerLives;
    }

    public Princess getPrincess() {
        try {
            return new Princess(this, currentWorld.findPrincess());
        } catch (PositionNotFoundException e) {
            return null;
        }
    }

    public boolean worldHasChanged() {
        return this.worldChanged;
    }

    public void worldChanged() {
        this.worldChanged = false;
    }

}

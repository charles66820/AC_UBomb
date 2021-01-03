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
    private Player player;
    private final String worldPath;
    private String worldPrefix;
    private String extension;
    private String theme;
    private int nbLevels;
    private int currentLevel = 0;
    private int initPlayerLives;
    private double startMonsterMoveFrequency;
    private double monsterMoveFrequencyRation;
    private double explosionCooldown;
    private double explosionDuration;
    private boolean worldChanged = false;
    private boolean smartAI = false;

    public Game(String worldPath) {
        this.worldPath = worldPath;
        this.worlds = new ArrayList<>();
        loadConfig(worldPath);
    }

    public void initGame() {
        // Load levels
        if (nbLevels == 0) {
            // Load static world for demo on levels is define to 0
            this.worlds.add(new WorldStatic());
        } else {
            try {
                for (int i = 1; i < nbLevels + 1; i++)
                    loadWorld(i);
            } catch (IOException ex) {
                System.err.println("Error on loading levels");
                // Load static world on levels loading error
                this.worlds.add(new WorldStatic());
            }
        }

        currentWorld = worlds.get(0);

        Position positionPlayer = null;
        try {
            positionPlayer = currentWorld.findPlayer();
            player = new Player(this, positionPlayer);
        } catch (
                PositionNotFoundException e) {
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
            this.startMonsterMoveFrequency = Integer.parseInt(prop.getProperty("startMonsterMoveFrequency", "800"));
            this.monsterMoveFrequencyRation = Integer.parseInt(prop.getProperty("monsterMoveFrequencyRation", "100"));
            this.explosionCooldown = Integer.parseInt(prop.getProperty("explosionCooldown", "4000"));
            this.explosionDuration = Integer.parseInt(prop.getProperty("explosionDuration", "400"));
            this.smartAI = Boolean.parseBoolean(prop.getProperty("smartAI", "true"));
            this.theme = prop.getProperty("theme", "default");
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
        double moveFrequency = startMonsterMoveFrequency - (monsterMoveFrequencyRation * levelNum);
        if (moveFrequency < 200) moveFrequency = 200;

        for (Position pos : world.getMonsterPositions()) {
            world.getMonsters().add(new Monster(this, pos, moveFrequency));
        }
        this.worlds.add(world);
    }

    public void goNextWord() {
        this.worldChanged = true;
        if (currentLevel + 1 < nbLevels) {
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

    public int getCurrentLevel() {
        return this.currentLevel;
    }

    // Getter and setters for settings
    public String getWorldPrefix() {
        return worldPrefix;
    }

    public void setWorldPrefix(String worldPrefix) {
        this.worldPrefix = worldPrefix;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public int getNbLevels() {
        return nbLevels;
    }

    public void setNbLevels(int nbLevels) {
        this.nbLevels = nbLevels;
    }

    public void setInitPlayerLives(int initPlayerLives) {
        this.initPlayerLives = initPlayerLives;
    }

    public double getStartMonsterMoveFrequency() {
        return startMonsterMoveFrequency;
    }

    public void setStartMonsterMoveFrequency(double startMonsterMoveFrequency) {
        this.startMonsterMoveFrequency = startMonsterMoveFrequency;
    }

    public double getMonsterMoveFrequencyRation() {
        return monsterMoveFrequencyRation;
    }

    public void setMonsterMoveFrequencyRation(double monsterMoveFrequencyRation) {
        this.monsterMoveFrequencyRation = monsterMoveFrequencyRation;
    }

    public double getExplosionCooldown() {
        return this.explosionCooldown;
    }

    public void setExplosionCooldown(double explosionCooldown) {
        this.explosionCooldown = explosionCooldown;
    }

    public void setExplosionDuration(double explosionDuration) {
        this.explosionDuration = explosionDuration;
    }

    public double getExplosionDuration() {
        return this.explosionDuration;
    }

    public boolean isSmartAI() {
        return smartAI;
    }

    public void setSmartAI(boolean smartAI) {
        this.smartAI = smartAI;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}

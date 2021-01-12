/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.*;
import java.util.*;

import fr.ubx.poo.model.decor.Door;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.character.Target;
import fr.ubx.poo.model.go.item.Bomb;
import fr.ubx.poo.view.image.ImageFactory;

public class Game {

    private World currentWorld; // the world where the player is
    private final List<World> worlds;
    private Player player;
    private final String worldPath;
    private String worldPrefix = "level"; // prefix of the level files
    private String extension = ".txt"; // extension of the level files
    private String theme = "default"; // name of the theme folder
    private String lang = "en"; // default language of the game
    private int nbLevels;
    private int currentLevel = 0;
    private int initPlayerLives = 1; // initial lives of the player
    private int startMonsterMoveFrequency = 800; // first level monster movement frequency in ms
    private int monsterMoveFrequencyRation = 100; // value that is added to the movements of monsters at each level
    private int explosionCooldown = 4000; // time of the bomb before explosion
    private int explosionDuration = 400; // duration of the explosion
    private int invulnerabilityDuration = 1000; // duration of the invulnerability of the player
    private boolean worldChanged = false;
    private boolean smartAI = false;
    private boolean isPrincess = false;

    public Game(String worldPath) {
        this.worldPath = worldPath;
        this.worlds = new ArrayList<>();
        loadConfig(worldPath);
    }

    public void initGame() {
        // Load levels
        if (nbLevels == 0) {
            // Load static world for demo when nbLevels is 0
            this.worlds.add(new WorldStatic());
        } else {
            try {
                for (int i = 1; i < nbLevels + 1; i++)
                    loadWorld(i);
            } catch (IOException ex) {
                System.err.println("Error on loading levels");
                // Load static world when an error is caught
                this.worlds.add(new WorldStatic());
            }
        }

        ImageFactory.getInstance().setPrincess(this.isPrincess);
        ImageFactory.getInstance().setTheme(this.theme);
        ImageFactory.getInstance().load();

        currentWorld = worlds.get(0);

        Position positionPlayer;
        try {
            positionPlayer = currentWorld.findPlayer();
            player = new Player(this, positionPlayer);
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

    }

    private void loadConfig(String path) {
        try (InputStream input = getClass().getResourceAsStream(path + "/config.properties")) {
            if (input == null) throw new IOException(path + "/config.properties (No such file or directory)");
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
            this.invulnerabilityDuration = Integer.parseInt(prop.getProperty("invulnerabilityDuration", "1000"));
            this.smartAI = Boolean.parseBoolean(prop.getProperty("smartAI", "true"));
            this.theme = prop.getProperty("theme", "default");
            this.lang = prop.getProperty("lang", "en");
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    /**
     * Load the world from the path of the file
     * @param levelNum The number of the level file to load
     * @throws IOException An IOException can be throw because of the file that we read
     */
    private void loadWorld(int levelNum) throws IOException {
        String filePath = this.worldPath + "/" + this.worldPrefix + levelNum + this.extension;
        InputStream input = getClass().getResourceAsStream(filePath);
        if (input == null) throw new IOException(filePath + "(No such file or directory)");

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
            world.getMonsters().add(new Monster(this, world, pos, moveFrequency));
        }
        this.worlds.add(world);
    }

    public void goNextWord() {
        this.worldChanged = true;
        if (currentLevel + 1 < nbLevels) {
            this.currentWorld = this.worlds.get(++currentLevel);
            // Search for the arrival door
            this.currentWorld.forEach((p, d) -> {
                if (d instanceof Door && !((Door) d).isNext()) player.setPosition(p);
            });
        }
    }

    public void goPrevWord() {
        this.worldChanged = true;
        if (currentLevel > 0) {
            this.currentWorld = this.worlds.get(--currentLevel);
            // Search for the previous door
            this.currentWorld.forEach((p, d) -> {
                if (d instanceof Door && ((Door) d).isNext()) player.setPosition(p);
            });
        }
    }

    public World getCurentWorld() {
        return currentWorld;
    }

    public List<Monster> getMonsters() {
        List<Monster> monsters = new ArrayList<>();
        for (World w : this.worlds) monsters.addAll(w.getMonsters());
        return monsters;
    }

    public List<Bomb> getBombs() {
        List<Bomb> bombs = new ArrayList<>();
        for (World w : this.worlds) bombs.addAll(w.getBombs());
        return bombs;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getInitPlayerLives() {
        return initPlayerLives;
    }

    public Target getTarget() {
        try {
            return new Target(this, currentWorld.findTarget());
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

    public int getStartMonsterMoveFrequency() {
        return startMonsterMoveFrequency;
    }

    public void setStartMonsterMoveFrequency(int startMonsterMoveFrequency) {
        this.startMonsterMoveFrequency = startMonsterMoveFrequency;
    }

    public int getMonsterMoveFrequencyRation() {
        return monsterMoveFrequencyRation;
    }

    public void setMonsterMoveFrequencyRation(int monsterMoveFrequencyRation) {
        this.monsterMoveFrequencyRation = monsterMoveFrequencyRation;
    }

    public int getExplosionCooldown() {
        return this.explosionCooldown;
    }

    public void setExplosionCooldown(int explosionCooldown) {
        this.explosionCooldown = explosionCooldown;
    }

    public void setExplosionDuration(int explosionDuration) {
        this.explosionDuration = explosionDuration;
    }

    public int getExplosionDuration() {
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

    public boolean isPrincess() {
        return isPrincess;
    }

    public void setPrincess(boolean princess) {
        isPrincess = princess;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getInvulnerabilityDuration() {
        return invulnerabilityDuration;
    }

    public void setInvulnerabilityDuration(int invulnerabilityDuration) {
        this.invulnerabilityDuration = invulnerabilityDuration;
    }
}

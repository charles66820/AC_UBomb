/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Door;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Target;
import fr.ubx.poo.model.go.item.Bomb;
import fr.ubx.poo.view.sprite.Sprite;
import fr.ubx.poo.view.sprite.SpriteFactory;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.character.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    private final List<Sprite> sprites = new ArrayList<>();
    private final List<Sprite> monstersSprites = new ArrayList<>();
    private final List<Sprite> bombsSprites = new ArrayList<>();
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;
    private Sprite spriteTarget;

    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        initialize(stage, game);
        buildAndSetGameLoop();
    }

    private void initialize(Stage stage, Game game) {
        this.stage = stage;
        Group root = new Group();
        layer = new Pane();

        int height = game.getWorld().dimension.height;
        int width = game.getWorld().dimension.width;
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/themes/" + this.game.getTheme() + "/css/application.css").toExternalForm());

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);

        removeSprites();
        createSprites();
    }

    private void createSprites() {
        // Create decor sprites
        game.getWorld().forEach((pos, d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        spritePlayer = SpriteFactory.createPlayer(layer, player);
        Target target = game.getTarget();
        if (target != null) spriteTarget = SpriteFactory.createTarget(layer, target);
        game.getWorld().getMonsters().forEach(monster -> monstersSprites.add(SpriteFactory.createMonster(layer, monster)));
        game.getWorld().getBombs().forEach(bomb -> bombsSprites.add(SpriteFactory.createBomb(layer, bomb)));
    }

    private void removeSprites() {
        this.sprites.forEach(Sprite::remove);
        this.sprites.clear();
        if (spritePlayer != null) spritePlayer.remove();
        if (spriteTarget != null) spriteTarget.remove();
        this.monstersSprites.forEach(Sprite::remove);
        this.monstersSprites.clear();
        this.bombsSprites.forEach(Sprite::remove);
        this.bombsSprites.clear();
    }

    protected final void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);

                // Do actions
                update(now);

                // Graphic update
                render();
                statusBar.update(game);
            }
        };
    }

    private void processInput(long now) {
        if (input.isExit()) {  //TODO: ajouter une interface sympa quand on quitte le jeu
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        }
        if (input.isMoveDown()) {
            player.requestMove(Direction.S);
        }
        if (input.isMoveLeft()) {
            player.requestMove(Direction.W);
        }
        if (input.isMoveRight()) {
            player.requestMove(Direction.E);
        }
        if (input.isMoveUp()) {
            player.requestMove(Direction.N);
        }
        if (input.isKey()) {
            Position nextPos = this.player.getDirection().nextPosition(this.player.getPosition());
            Decor decor = this.game.getWorld().get(nextPos);
            if (decor instanceof Door) {
                Door door = (Door) decor;
                if (!door.isOpen() && (this.player.getKey() >= 1)) {
                    door.setOpen(true);
                    this.game.getWorld().setChanged(true);
                    this.player.setKey(this.player.getKey() - 1);
                }
            }
        }
        if (input.isBomb()) {
            Position pos = this.player.getPosition();
            if (this.player.getBomb() >= 1) { //TODO: une bombe ne peut se poser que sur un slot vide ou un monstre !
                Bomb b = new Bomb(this.game, pos, now);
                this.game.getWorld().getBombs().add(b); // add bomb in bomb list
                player.setBomb(player.getBomb() - 1);
                this.bombsSprites.add(SpriteFactory.createBomb(layer, b)); // add sprite of the current bomb in sprite bomb list
            }
        }
        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }


    private void update(long now) {
        player.update(now);

        if (!player.isAlive()) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }
        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Gagné", Color.BLUE);
        }

        // Update all bombs in the game
        List<Bomb> explodedBomb = new ArrayList<>();
        for (Bomb b : this.game.getWorld().getBombs()) {
            b.update(now);
            if (b.canBeRemove()) {
                explodedBomb.add(b);
                this.game.getWorld().setChanged(true);
            }
        }
        this.game.getWorld().getBombs().removeAll(explodedBomb);

        // Update monster
        List<Monster> removeMonster = new ArrayList<>();
        for (Monster b : this.game.getWorld().getMonsters()) {
            b.update(now);
            if (!b.isAlive()) removeMonster.add(b);
        }
        this.game.getWorld().getMonsters().removeAll(removeMonster);

        if (this.game.worldHasChanged()) {
            initialize(stage, game);
            this.game.worldChanged();
            statusBar.setGameLevel(this.game.getCurrentLevel() + 1);
        }

        // Update all sprites when world has changed
        if (this.game.getWorld().hasChanged()) {
            removeSprites();
            createSprites();
            this.game.getWorld().setChanged(false);
        }

    }

    private void render() {
        sprites.forEach(Sprite::render);
        monstersSprites.forEach(Sprite::render);
        bombsSprites.forEach(Sprite::render);
        // last rendering to have player in the foreground
        spritePlayer.render();
        if (spriteTarget != null) spriteTarget.render();
    }

    public void start() {
        gameLoop.start();
    }
}

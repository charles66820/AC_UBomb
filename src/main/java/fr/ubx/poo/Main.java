/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo;

import fr.ubx.poo.engine.GameEngine;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private Game game;

    @Override
    public void start(Stage stage) {
        ImageFactory.getInstance().load();
        String path = getClass().getResource("/sample").getFile();
        game = new Game(path);
        initMainMenu(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void initMainMenu(Stage stage) {
        // UI elements4
        // Title
        StackPane titlePane = new StackPane();
        Text title = new Text("Main menu");
        title.getStyleClass().add("title");
        title.setTextAlignment(TextAlignment.CENTER);
        titlePane.getChildren().add(title);
        StackPane.setAlignment(title, Pos.CENTER);

        // Play button
        Button startBtn = new Button("Lancer le jeu");
        startBtn.setOnAction(e -> {
            game.initFirstWorldAndPlayer();
            GameEngine engine = new GameEngine("UBomb : in game", game, stage);
            engine.start();
        });

        // Choose prince or princess
        ToggleGroup playerGroup = new ToggleGroup();
        RadioButton princeRadio = new RadioButton("Pince");
        HBox.setMargin(princeRadio, new Insets(8, 8, 8, 8));
        princeRadio.setToggleGroup(playerGroup);
        RadioButton princessRadio = new RadioButton("Princess");
        HBox.setMargin(princessRadio, new Insets(8, 8, 8, 8));
        princessRadio.setToggleGroup(playerGroup);
        princessRadio.fire();
        HBox playerHBox = new HBox();
        playerHBox.setAlignment(Pos.CENTER);
        playerHBox.getChildren().addAll(princessRadio, princeRadio);

        // Themes
        ChoiceBox<String> themesChoiceBox = new ChoiceBox<>();
        themesChoiceBox.getItems().addAll
                ("Classic", "Miraculous ladybug");
        themesChoiceBox.getSelectionModel().select(0);

        // Settings panel
        StackPane settingPane = initSettingsPanel();

        // Settings
        Button settingsBtn = new Button("Settings");
        settingsBtn.setOnAction(e -> {
            settingPane.setVisible(true);
            // open with animation
            FadeTransition ft = new FadeTransition(Duration.millis(300), settingPane);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        });

        // Layout
        VBox menuVBox = new VBox(title, startBtn, playerHBox, themesChoiceBox, settingsBtn);
        menuVBox.setAlignment(Pos.CENTER);
        menuVBox.setSpacing(8);

        StackPane root = new StackPane();
        root.getChildren().addAll(menuVBox, settingPane);

        Scene scene = new Scene(root, 500, 200);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        stage.setTitle("UBomb : main menu");
        stage.setMinWidth(500);
        stage.setMinHeight(400);
        stage.setScene(scene);
        stage.show();
    }

    private StackPane initSettingsPanel() {
        // Close button
        Button closeBtn = new Button("Save and quite");
        AnchorPane closePane = new AnchorPane();
        AnchorPane.setRightAnchor(closeBtn, 16.0);
        AnchorPane.setBottomAnchor(closeBtn, 16.0);
        closePane.getChildren().add(closeBtn);

        // Levels files prefix
        Text prefixLabel = new Text("Levels files prefix");
        TextField prefixTextField = new TextField();
        prefixTextField.setText(this.game.getWorldPrefix());

        // Number of levels
        Text nbLevelsLabel = new Text("Number of levels");
        TextField nbLevelsTextField = new TextField();
        nbLevelsTextField.setText(String.valueOf(this.game.getNbLevels()));

        // Levels files extension
        Text extensionLabel = new Text("Levels files extension");
        TextField extensionTextField = new TextField();
        extensionTextField.setText(this.game.getExtension());

        // Initials lives
        Text initialsLivesLabel = new Text("Initials lives");
        TextField initialsLivesTextField = new TextField();
        initialsLivesTextField.setText(String.valueOf(this.game.getInitPlayerLives()));

        // Start monster move frequency
        Text startMonsterMoveFrequencyLabel = new Text("monster move frequency (in ms)");
        TextField startMonsterMoveFrequencyTextField = new TextField();
        startMonsterMoveFrequencyTextField.setText(String.valueOf(this.game.getStartMonsterMoveFrequency()));

        // Monster move frequency ration add on each level
        Text monsterMoveFrequencyRationLabel = new Text("monster move frequency ration (in ms)");
        TextField monsterMoveFrequencyRationTextField = new TextField();
        monsterMoveFrequencyRationTextField.setText(String.valueOf(this.game.getMonsterMoveFrequencyRation()));

        // Explosion cooldown for bomb
        Text explosionCooldownLabel = new Text("Explosion cooldown (in ms)");
        TextField explosionCooldownTextField = new TextField();
        explosionCooldownTextField.setText(String.valueOf(this.game.getExplosionCooldown()));

        // Explosion duration for bomb
        Text explosionDurationLabel = new Text("Explosion duration (in ms)");
        TextField explosionDurationTextField = new TextField();
        explosionDurationTextField.setText(String.valueOf(this.game.getExplosionDuration()));

        // Choose monsterAI
        Text monsterAILabel = new Text("Choose monster AI");
        ToggleGroup monsterAIGroup = new ToggleGroup();
        RadioButton randomAIRadio = new RadioButton("Random AI");
        HBox.setMargin(randomAIRadio, new Insets(8, 8, 8, 8));
        randomAIRadio.setToggleGroup(monsterAIGroup);
        RadioButton smartAIRadio = new RadioButton("Smart AI");
        HBox.setMargin(smartAIRadio, new Insets(8, 8, 8, 8));
        smartAIRadio.setToggleGroup(monsterAIGroup);
        smartAIRadio.fire();
        HBox monsterAIHBox = new HBox();
        monsterAIHBox.setAlignment(Pos.CENTER);
        monsterAIHBox.getChildren().addAll(smartAIRadio, randomAIRadio);

        // Layout
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(500, 200);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(2);
        gridPane.setVgap(8);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(prefixLabel, 0, 0);
        gridPane.add(prefixTextField, 1, 0);
        gridPane.add(nbLevelsLabel, 0, 1);
        gridPane.add(nbLevelsTextField, 1, 1);
        gridPane.add(extensionLabel, 0, 2);
        gridPane.add(extensionTextField, 1, 2);
        gridPane.add(initialsLivesLabel, 0, 3);
        gridPane.add(initialsLivesTextField, 1, 3);
        gridPane.add(startMonsterMoveFrequencyLabel, 0, 4);
        gridPane.add(startMonsterMoveFrequencyTextField, 1, 4);
        gridPane.add(monsterMoveFrequencyRationLabel, 0, 5);
        gridPane.add(monsterMoveFrequencyRationTextField, 1, 5);
        gridPane.add(explosionCooldownLabel, 0, 6);
        gridPane.add(explosionCooldownTextField, 1, 6);
        gridPane.add(explosionDurationLabel, 0, 7);
        gridPane.add(explosionDurationTextField, 1, 7);
        gridPane.add(monsterAILabel, 0, 8);
        gridPane.add(monsterAIHBox, 1, 8);
        gridPane.add(closePane, 1, 9);

        StackPane settingPane = new StackPane(gridPane);
        settingPane.setStyle("-fx-background-color:#FFFFFFFF");
        settingPane.setVisible(false);
        closeBtn.setOnMouseClicked(e -> {
            if (settingPane.isVisible()) {
                // Close with animation
                FadeTransition ft = new FadeTransition(Duration.millis(300), settingPane);
                ft.setOnFinished(e1 -> settingPane.setVisible(false));
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                ft.play();
            }
        });

        return settingPane;
    }
}

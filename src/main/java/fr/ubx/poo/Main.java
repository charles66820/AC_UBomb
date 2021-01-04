/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo;

import fr.ubx.poo.engine.GameEngine;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.utils.Lang;
import fr.ubx.poo.utils.LangFactory;
import fr.ubx.poo.utils.Theme;
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
    private final int WindowMinHeight = 600;
    private final int WindowMinWidth = 450;
    private Game game;
    private String styleRessources;
    Scene scene;
    private boolean reloadUI = false;

    @Override
    public void start(Stage stage) {
        String path = getClass().getResource("/sample").getFile();
        this.game = new Game(path);

        LangFactory.getInstance().setLang(this.game.getTheme(), "en");

        initMainMenu(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void initMainMenu(Stage stage) {
        // Load lang
        LangFactory.getInstance().setLang(this.game.getTheme(), this.game.getLang());

        // UI elements4
        // Title
        StackPane titlePane = new StackPane();
        Text title = new Text(LangFactory.get("mainMenu"));
        title.getStyleClass().add("title");
        title.setTextAlignment(TextAlignment.CENTER);
        titlePane.getChildren().add(title);
        StackPane.setAlignment(title, Pos.CENTER);

        // Play button
        Button startBtn = new Button(LangFactory.get("startBtn"));
        startBtn.setOnAction(e -> {
            game.initGame();
            GameEngine engine = new GameEngine(LangFactory.get("inGameTitle"), game, stage);
            engine.start();
        });

        // Choose prince or princess
        ToggleGroup playerGroup = new ToggleGroup();
        RadioButton princeRadio = new RadioButton(LangFactory.get("prince"));
        HBox.setMargin(princeRadio, new Insets(8, 8, 8, 8));
        princeRadio.setToggleGroup(playerGroup);
        RadioButton princessRadio = new RadioButton(LangFactory.get("princess"));
        HBox.setMargin(princessRadio, new Insets(8, 8, 8, 8));
        princessRadio.setToggleGroup(playerGroup);

        if (this.game.isPrincess()) princessRadio.setSelected(true);
        else princeRadio.setSelected(true);

        HBox playerHBox = new HBox();
        playerHBox.setAlignment(Pos.CENTER);
        playerHBox.getChildren().addAll(princessRadio, princeRadio);

        playerGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) ->
                this.game.setPrincess(newValue == princessRadio)
        );

        // Themes
        Text themesLabel = new Text(LangFactory.get("themes"));
        ChoiceBox<Theme> themesChoiceBox = new ChoiceBox<>();
        themesChoiceBox.getItems().addAll(Theme.values());
        Theme themeFormConfig = Theme.get(this.game.getTheme());
        if (themeFormConfig != null)
            themesChoiceBox.getSelectionModel().select(themeFormConfig);
        else themesChoiceBox.getSelectionModel().select(0);
        themesChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, lastTheme, newTheme) -> {
                    this.game.setTheme(newTheme.name);
                    scene.getStylesheets().remove(this.styleRessources);
                    this.styleRessources = getClass().getResource("/themes/" + newTheme.name + "/css/application.css").toExternalForm();
                    this.scene.getStylesheets().add(styleRessources);
                    // Load lang
                    LangFactory.getInstance().setLang(newTheme.name, this.game.getLang());
                    this.initMainMenu(stage);
                });
        HBox themeHBox = new HBox();
        themeHBox.setAlignment(Pos.CENTER);
        themeHBox.setSpacing(8);
        themeHBox.getChildren().addAll(themesLabel, themesChoiceBox);

        // Settings panel
        StackPane settingPane = initSettingsPanel(stage);

        // Settings
        Button settingsBtn = new Button(LangFactory.get("settings"));
        settingsBtn.setOnAction(e -> {
            settingPane.setVisible(true);
            // open with animation
            FadeTransition ft = new FadeTransition(Duration.millis(300), settingPane);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        });

        // Layout
        VBox menuVBox = new VBox(title, startBtn, playerHBox, themeHBox, settingsBtn);
        menuVBox.setAlignment(Pos.CENTER);
        menuVBox.setSpacing(8);

        StackPane root = new StackPane();
        root.getChildren().addAll(menuVBox, settingPane);

        this.scene = new Scene(root, WindowMinHeight, WindowMinWidth);
        this.styleRessources = getClass().getResource("/themes/" + this.game.getTheme() + "/css/application.css").toExternalForm();
        this.scene.getStylesheets().add(styleRessources);

        if (this.reloadUI) {
            settingPane.setVisible(true);
            this.reloadUI = false;
        }

        stage.setTitle(LangFactory.get("mainMenuTitle"));
        stage.setMinWidth(WindowMinHeight);
        stage.setMinHeight(WindowMinWidth);
        stage.setScene(scene);
        stage.show();
    }

    private StackPane initSettingsPanel(Stage stage) {
        // Levels files prefix
        Text prefixLabel = new Text(LangFactory.get("prefix"));
        TextField prefixTextField = new TextField();
        prefixTextField.setText(this.game.getWorldPrefix());

        // Number of levels
        Text nbLevelsLabel = new Text(LangFactory.get("nbLevels"));
        TextField nbLevelsTextField = new TextField();
        nbLevelsTextField.setText(String.valueOf(this.game.getNbLevels()));
        nbLevelsTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,12}")) nbLevelsTextField.setText(oldValue);
        });

        // Levels files extension
        Text extensionLabel = new Text(LangFactory.get("extension"));
        TextField extensionTextField = new TextField();
        extensionTextField.setText(this.game.getExtension());

        // Initials lives
        Text initialsLivesLabel = new Text(LangFactory.get("initialsLives"));
        TextField initialsLivesTextField = new TextField();
        initialsLivesTextField.setText(String.valueOf(this.game.getInitPlayerLives()));
        initialsLivesTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,12}"))
                initialsLivesTextField.setText(oldValue);
        });

        // Start monster move frequency
        Text startMonsterMoveFrequencyLabel = new Text(LangFactory.get("startMonsterMoveFrequency"));
        TextField startMonsterMoveFrequencyTextField = new TextField();
        startMonsterMoveFrequencyTextField.setText(String.valueOf(this.game.getStartMonsterMoveFrequency()));
        startMonsterMoveFrequencyTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?"))
                startMonsterMoveFrequencyTextField.setText(oldValue);
        });

        // Monster move frequency ration add on each level
        Text monsterMoveFrequencyRationLabel = new Text(LangFactory.get("monsterMoveFrequencyRation"));
        TextField monsterMoveFrequencyRationTextField = new TextField();
        monsterMoveFrequencyRationTextField.setText(String.valueOf(this.game.getMonsterMoveFrequencyRation()));
        monsterMoveFrequencyRationTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?"))
                monsterMoveFrequencyRationTextField.setText(oldValue);
        });

        // Explosion cooldown for bomb
        Text explosionCooldownLabel = new Text(LangFactory.get("explosionCooldown"));
        TextField explosionCooldownTextField = new TextField();
        explosionCooldownTextField.setText(String.valueOf(this.game.getExplosionCooldown()));
        explosionCooldownTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?"))
                explosionCooldownTextField.setText(oldValue);
        });

        // Explosion duration for bomb
        Text explosionDurationLabel = new Text(LangFactory.get("explosionDuration"));
        TextField explosionDurationTextField = new TextField();
        explosionDurationTextField.setText(String.valueOf(this.game.getExplosionDuration()));
        explosionDurationTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?"))
                explosionDurationTextField.setText(oldValue);
        });

        // Choose monsterAI
        Text monsterAILabel = new Text(LangFactory.get("monsterAI"));
        ToggleGroup monsterAIGroup = new ToggleGroup();
        RadioButton randomAIRadio = new RadioButton(LangFactory.get("randomAI"));
        HBox.setMargin(randomAIRadio, new Insets(8, 8, 8, 8));
        randomAIRadio.setToggleGroup(monsterAIGroup);
        RadioButton smartAIRadio = new RadioButton(LangFactory.get("smartAI"));
        HBox.setMargin(smartAIRadio, new Insets(8, 8, 8, 8));
        smartAIRadio.setToggleGroup(monsterAIGroup);

        if (this.game.isSmartAI()) smartAIRadio.setSelected(true);
        else randomAIRadio.setSelected(true);

        HBox monsterAIHBox = new HBox();
        monsterAIHBox.setAlignment(Pos.CENTER);
        monsterAIHBox.getChildren().addAll(smartAIRadio, randomAIRadio);

        // Themes
        Text langLabel = new Text(LangFactory.get("lang"));
        ChoiceBox<Lang> langChoiceBox = new ChoiceBox<>();
        langChoiceBox.getItems().addAll(Lang.values());
        Lang langFormConfig = Lang.get(this.game.getLang());
        if (langFormConfig != null)
            langChoiceBox.getSelectionModel().select(langFormConfig);
        else langChoiceBox.getSelectionModel().select(0);
        langChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, lastLang, newLang) -> {
                    this.game.setLang(newLang.code);
                    // Load new lang
                    LangFactory.getInstance().setLang(this.game.getTheme(), newLang.code);
                    this.reloadUI = true;
                    this.initMainMenu(stage);
                });
        HBox langHBox = new HBox();
        langHBox.setAlignment(Pos.CENTER);
        langHBox.setSpacing(8);
        langHBox.getChildren().addAll(langLabel, langChoiceBox);

        // Close button
        Button closeBtn = new Button(LangFactory.get("closeSettingsBtn"));
        AnchorPane closePane = new AnchorPane();
        AnchorPane.setRightAnchor(closeBtn, 16.0);
        AnchorPane.setBottomAnchor(closeBtn, 16.0);
        closePane.getChildren().add(closeBtn);

        // Layout
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(WindowMinHeight, WindowMinWidth);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(2);
        gridPane.setVgap(10);
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
        gridPane.add(langLabel, 0, 9);
        gridPane.add(langHBox, 1, 9);
        gridPane.add(closePane, 1, 10);

        StackPane settingPane = new StackPane(gridPane);
        settingPane.setStyle("-fx-background-color:#FFFFFFFF");
        settingPane.setVisible(false);

        closeBtn.setOnAction(e -> {
            if (settingPane.isVisible()) {
                // Validation
                if (nbLevelsTextField.getText().equals(""))
                    nbLevelsTextField.setText("0");

                if (initialsLivesTextField.getText().equals(""))
                    initialsLivesTextField.setText("0");

                if (startMonsterMoveFrequencyTextField.getText().equals(""))
                    startMonsterMoveFrequencyTextField.setText("800");

                if (monsterMoveFrequencyRationTextField.getText().equals(""))
                    monsterMoveFrequencyRationTextField.setText("100");

                if (explosionCooldownTextField.getText().equals(""))
                    explosionCooldownTextField.setText("4000");

                if (explosionDurationTextField.getText().equals(""))
                    explosionDurationTextField.setText("400");

                // Save value
                this.game.setWorldPrefix(prefixTextField.getText());
                this.game.setNbLevels(Integer.parseInt(nbLevelsTextField.getText()));
                this.game.setExtension(extensionTextField.getText());
                this.game.setInitPlayerLives(Integer.parseInt(initialsLivesTextField.getText()));
                this.game.setStartMonsterMoveFrequency(Double.parseDouble(startMonsterMoveFrequencyTextField.getText()));
                this.game.setMonsterMoveFrequencyRation(Double.parseDouble(monsterMoveFrequencyRationTextField.getText()));
                this.game.setExplosionCooldown(Double.parseDouble(explosionCooldownTextField.getText()));
                this.game.setExplosionDuration(Double.parseDouble(explosionDurationTextField.getText()));
                this.game.setSmartAI(monsterAIGroup.getSelectedToggle() == smartAIRadio);

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

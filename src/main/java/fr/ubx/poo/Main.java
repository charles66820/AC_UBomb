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
import javafx.scene.paint.Color;
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
            GameEngine engine = new GameEngine("UBomb : in game", game, stage);
            engine.start();
        });

        // Chose prince or princess
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

        Scene scene = new Scene(root, 400, 200);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        stage.setTitle("UBomb : main menu");
        stage.setMinHeight(400);
        stage.setMinWidth(400);
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

        // TODO: add form
        Button btnBtn = new Button("tmp btn");

        // Layout
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(400, 200);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(2);
        gridPane.setVgap(8);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(btnBtn, 0, 0);

        StackPane settingPane = new StackPane(gridPane, closePane);
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

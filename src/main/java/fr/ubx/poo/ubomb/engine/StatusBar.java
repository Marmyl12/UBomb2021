/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.engine;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.view.ImageResource;
import fr.ubx.poo.ubomb.view.Sprite;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class StatusBar {
    public static final int height = 55;
    public static final int width = 12 * Sprite.size;
    private final Game game;
    private final DropShadow ds = new DropShadow();
    private final HBox hBox = new HBox();
    private final Text lives = new Text();
    private final Text availableBombs = new Text();
    private final Text bombRange = new Text();
    private final Text keys = new Text();
    private final HBox level = new HBox();
    private final int gameLevel = 1;


    public StatusBar(Group root, int sceneWidth, int sceneHeight, Game game) {
        // Status bar
        this.game = game;


        level.getStyleClass().add("level");
        level.getChildren().add(new ImageView(ImageResource.getDigit(gameLevel)));

        ds.setRadius(5.0);
        ds.setOffsetX(3.0);
        ds.setOffsetY(3.0);
        ds.setColor(Color.color(0.5f, 0.5f, 0.5f));


        HBox status = new HBox();
        status.getStyleClass().add("status");
        HBox live = statusGroup(ImageResource.HEART.getImage(), this.lives);
        HBox bombs = statusGroup(ImageResource.BANNER_BOMB.getImage(), availableBombs);
        HBox range = statusGroup(ImageResource.BANNER_RANGE.getImage(), bombRange);
        HBox key = statusGroup(ImageResource.KEY.getImage(), keys);
        status.setSpacing(40.0);
        status.getChildren().addAll(live, bombs, range, key);

        hBox.getChildren().addAll(level, status);
        hBox.getStyleClass().add("statusBar");
        hBox.relocate(0, sceneHeight);

        double scale = (double) (sceneWidth) / ((double) (12 * Sprite.size));
        hBox.setScaleX(scale);
        hBox.setScaleY(scale);
        hBox.relocate(0d + (scale - 1d) * width / 2d, sceneHeight + (scale - 1d) * height / 2d);

        hBox.setPrefSize(width, height);
        hBox.resize(width, height);

        root.getChildren().add(hBox);
    }

    private void updateLevel(int n) {
        if (n != gameLevel) {
            level.getChildren().clear();
            level.getChildren().add(new ImageView(ImageResource.getDigit(n)));
        }
    }

    private HBox statusGroup(Image kind, Text number) {
        HBox group = new HBox();
        ImageView img = new ImageView(kind);
        img.setPreserveRatio(true);
        group.setSpacing(4);
        number.setEffect(ds);
        number.setCache(true);
        number.setFill(Color.BLACK);
        number.getStyleClass().add("number");
        group.getChildren().addAll(img, number);
        return group;
    }

    public void update(Game game) {
        updateLevel(game.getCurrentLevel()+1);
        Player player = game.getPlayer();
        lives.setText(String.valueOf(player.getLives()));
        bombRange.setText(String.valueOf(player.getBombRange()));
        availableBombs.setText(String.valueOf(player.getAvailableBombs()));
        keys.setText(String.valueOf(player.getKeys()));
    }

}

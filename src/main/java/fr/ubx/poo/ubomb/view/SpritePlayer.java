/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.view;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.go.entity.character.Player;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpritePlayer extends Sprite {


    private final ColorAdjust effect = new ColorAdjust();

    public SpritePlayer(Pane layer, Player player) {
        super(layer, null, player);
        effect.setBrightness(0.8);
        updateImage();
    }

    @Override
    public void updateImage() {
        Player player = (Player) getGameObject();
        Image image = getImage(player.getDirection());
        if (player.isInvincible()) setImage(image, effect);
        else setImage(image);
        getGameObject().setModified(true);
    }

    public Image getImage(Direction direction) {
        return ImageResource.getPlayer(direction);
    }
}

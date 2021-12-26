package fr.ubx.poo.ubomb.view;

import fr.ubx.poo.ubomb.go.Bomb;
import fr.ubx.poo.ubomb.go.character.Player;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteBomb  extends Sprite {

    public SpriteBomb(Pane layer, Bomb bomb) {
        super(layer, ImageResource.getBomb(0), bomb);
        updateImage();
    }

    public void updateImage() {
        Bomb bomb = (Bomb) getGameObject();
        if (bomb.hasPhaseChanged()) {
            setImage(ImageResource.getBomb(bomb.getPhase()));
            bomb.setModified(true);
        }
    }
}

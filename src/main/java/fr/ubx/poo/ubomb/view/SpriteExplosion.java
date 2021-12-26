package fr.ubx.poo.ubomb.view;

import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.decor.Explosion;
import javafx.scene.layout.Pane;

public class SpriteExplosion extends Sprite {
    public SpriteExplosion(Pane layer, GameObject gameObject) {
        super(layer, ImageResource.EXPLOSION.getImage(), gameObject);
    }

    @Override
    public void updateImage() {
        ((Explosion) getGameObject()).update();
    }
}

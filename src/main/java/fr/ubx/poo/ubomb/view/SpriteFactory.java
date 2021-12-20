/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.view;

import fr.ubx.poo.ubomb.go.Bomb;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.Box;
import fr.ubx.poo.ubomb.go.decor.bonus.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import static fr.ubx.poo.ubomb.view.ImageResource.*;


public final class SpriteFactory {

    public static Sprite create(Pane layer, GameObject gameObject) {
        if (gameObject instanceof Stone)
            return new Sprite(layer, STONE.getImage(), gameObject);
        if (gameObject instanceof Tree)
            return new Sprite(layer, TREE.getImage(), gameObject);
        if (gameObject instanceof Key)
            return new Sprite(layer, KEY.getImage(), gameObject);
        if (gameObject instanceof Box)
            return new Sprite(layer, BOX.getImage(), gameObject);
        if (gameObject instanceof Heart)
            return new Sprite(layer, HEART.getImage(), gameObject);
        if (gameObject instanceof BombRangeInc)
            return new Sprite(layer, BONUS_BOMB_RANGE_INC.getImage(), gameObject);
        if (gameObject instanceof BombRangeDec)
            return new Sprite(layer, BONUS_BOMB_RANGE_DEC.getImage(), gameObject);
        if (gameObject instanceof BombNbInc)
            return new Sprite(layer, BONUS_BOMB_NB_INC.getImage(), gameObject);
         if (gameObject instanceof BombNbDec)
            return new Sprite(layer, BONUS_BOMB_RANGE_DEC.getImage(), gameObject);
        if (gameObject instanceof Princess)
            return new Sprite(layer, PRINCESS.getImage(), gameObject);
        if (gameObject instanceof Monster)
            return new Sprite(layer, MONSTER_DOWN.getImage(), gameObject);
        if (gameObject instanceof DoorNextClosed)
            return new Sprite(layer, DOOR_CLOSED.getImage(), gameObject);
        if (gameObject instanceof DoorNextOpened)
            return new Sprite(layer, DOOR_OPENED.getImage(), gameObject);
        if (gameObject instanceof Bomb) {
            return new Sprite(layer, BOMB_3.getImage(), gameObject);
        }


        throw new RuntimeException("Unsupported sprite for decor " + gameObject);
    }

    //return the image that the sprite should use for the current phase
    public static Image getPhaseImage(Bomb bomb) {
        switch (bomb.getPhase()) {
            case 0: return BOMB_3.getImage();
            case 1: return BOMB_2.getImage();
            case 2: return BOMB_1.getImage();
            case 3: return BOMB_0.getImage();
            default: return EXPLOSION.getImage();
        }
    }
}

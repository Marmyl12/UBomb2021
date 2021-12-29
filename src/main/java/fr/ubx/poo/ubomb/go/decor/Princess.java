/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Character;
import fr.ubx.poo.ubomb.go.character.Player;

public class Princess extends Decor {

    public Princess(Position position) {
        super(position);
    }

    @Override
    public boolean isWalkable(Character character) {
        return character instanceof Player;
    }

}

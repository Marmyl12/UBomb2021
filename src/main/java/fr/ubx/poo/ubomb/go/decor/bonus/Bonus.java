/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.Takeable;
import fr.ubx.poo.ubomb.go.entity.character.Character;
import fr.ubx.poo.ubomb.go.entity.character.Player;
import fr.ubx.poo.ubomb.go.decor.Decor;

public abstract class Bonus extends Decor implements Takeable {
    public Bonus(Position position) {
        super(position);
    }

    @Override
    public boolean isWalkable(Character character) { return character instanceof Player; }

    @Override
    public void explode() {
        remove();
    }
}

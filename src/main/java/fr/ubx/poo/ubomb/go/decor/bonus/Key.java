/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.entity.character.Player;

public class Key extends Bonus {
    public Key(Position position) {
        super(position);
    }

    public void takenBy(Player player) { player.takeKey(); }

    @Override
    public void explode() {  }
}

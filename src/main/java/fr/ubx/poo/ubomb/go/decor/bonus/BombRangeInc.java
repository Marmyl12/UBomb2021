package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.entity.character.Player;

public class BombRangeInc extends Bonus {
    public BombRangeInc(Position position) {
        super(position);
    }

    public void takenBy(Player player) { player.takeBombRangeInc(); }
}

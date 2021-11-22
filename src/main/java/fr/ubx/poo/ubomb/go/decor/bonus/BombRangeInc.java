package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.Decor;

public class BombRangeInc extends Bonus {
    public BombRangeInc(Position position) {
        super(position);
    }

    public void takenBy(Player player) {
        player.takeBombRangeInc();
    }
}

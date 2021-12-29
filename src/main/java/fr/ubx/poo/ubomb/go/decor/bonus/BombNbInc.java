package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.entity.character.Player;

public class BombNbInc extends Bonus {
    public BombNbInc(Position position) {
        super(position);
    }

    public void takenBy(Player player) { player.BombNumberInc(); }
}

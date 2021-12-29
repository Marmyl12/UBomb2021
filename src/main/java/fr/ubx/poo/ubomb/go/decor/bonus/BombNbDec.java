package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.entity.character.Player;

public class BombNbDec extends Bonus {
    public BombNbDec(Position position) {
        super(position);
    }

    public void takenBy(Player player) {
        player.BombNumberDec();
    }
}

package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class DoorNextClosed extends Decor {
    public DoorNextClosed(Position position) { super(position); }

    @Override
    public boolean isWalkable(Player player) {
        return false;
    }
}

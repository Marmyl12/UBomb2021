package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class Box extends Decor {
    public Box(Position position) {
        super(position);
    }
    @Override
    public boolean isWalkable(Player player) {
        return false;
    }

}

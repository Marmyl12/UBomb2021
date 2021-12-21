package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Character;

public class DoorPrevOpened extends Decor {
    public DoorPrevOpened(Position position) { super(position); }

    @Override
    public boolean isWalkable(Character character) { return true; }
}

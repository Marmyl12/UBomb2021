package fr.ubx.poo.ubomb.go;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Character;

public class Bomb extends GameObject {


    public Bomb(Position position) {
        super(position);
    }




    @Override
    public boolean isWalkable(Character character) {
        return false;
    }
}

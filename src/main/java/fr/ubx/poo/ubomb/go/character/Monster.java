/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.*;

public class Monster extends Character  {

    public Monster(Game game, Position position, int lives) { super(game, position, lives); }

@Override

    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        GameObject obj = game.getGrid().get(nextPos);
        //Check collision with obstacle
        if (obj != null) {
            //exceptions
            if(obj instanceof Princess||obj instanceof DoorNextOpened || obj instanceof DoorPrevOpened)
                return false;
            return obj.isWalkable(this);
        }
        int height = game.getGrid().getHeight();
        int width = game.getGrid().getWidth();
        //Check collision with the grid
        return nextPos.getX() >= 0 && nextPos.getY() >= 0 && nextPos.getX() < width && nextPos.getY() < height;
    }







}

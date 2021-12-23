package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.character.Character;
import fr.ubx.poo.ubomb.go.character.Player;

public class Box extends Decor implements Movable {
    public Box(Position position) {
        super(position);
    }

    public Box(Game game, Position position) { super(game, position); }

    @Override
    public boolean isWalkable(Character character) {
        return false;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        GameObject obj = game.getGrid().get(nextPos);
        //Check collision with obstacle
        if (obj != null) return false;
        int height = game.getGrid().getHeight();
        int width = game.getGrid().getWidth();
        //Check collision with the grid
        return nextPos.getX() >= 0 && nextPos.getY() >= 0 && nextPos.getX() < width && nextPos.getY() < height;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        game.getGrid().remove(getPosition());
        setPosition(nextPos);
        game.getGrid().set(nextPos, this);
    }

}

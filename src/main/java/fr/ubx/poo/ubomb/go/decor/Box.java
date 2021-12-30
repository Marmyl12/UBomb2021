package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.entity.Entity;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.entity.Explosion;
import fr.ubx.poo.ubomb.go.entity.character.Character;

import java.util.List;

public class Box extends Decor implements Movable {

    public Box(Game game, Position position) {
        super(game, position);
    }

    @Override
    public boolean isWalkable(Character character) {
        return false;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        GameObject obj = game.getGrid().get(nextPos);
        List<Entity> ent = game.getGameObjects(nextPos);
        //Check collision with obstacle
        for (GameObject e : ent){
            if (e != null && !(e instanceof Explosion)) return false;
        }
        if (obj != null) return false;
        //Check collision with the grid
        return game.inside(nextPos);
    }

    @Override
    public void explode() {
        remove();
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        game.getGrid().remove(getPosition());
        setPosition(nextPos);
        game.getGrid().set(nextPos, this);
    }

}

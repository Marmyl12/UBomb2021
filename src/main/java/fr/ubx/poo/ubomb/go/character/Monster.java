/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.Bomb;
import fr.ubx.poo.ubomb.go.Entity;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.*;

import java.util.List;

public class Monster extends Character {

    private long timeSinceLastMove;
    private final long baseVelocity = 10_000_000_000L;

    public Monster(Game game, Position position, int level, int lives) {
        super(game, position, level, lives);
        timeSinceLastMove = System.nanoTime();
    }

    @Override
    public boolean canMove(Direction direction) {
        List<Entity> entities = game.getGameObjects(direction.nextPosition(getPosition()), getLevel());
        for (Entity entity : entities) {
            if (entity instanceof Bomb || entity instanceof  Monster)
                return false;
        }
        return super.canMove(direction);
    }

    @Override
    public void update(long now) {
        if (getLives() <= 0) this.remove();
        if (System.nanoTime() > timeSinceLastMove + baseVelocity / (game.monsterVelocity + getLevel() * 5)) {
            requestMove(Direction.random());
            timeSinceLastMove = System.nanoTime();
        }
        super.update(now);

    }

}

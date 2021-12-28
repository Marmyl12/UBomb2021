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

public class Monster extends Character {

    private long timeSinceLastMove;
    private final long baseVelocity = 10_000_000_000L;

    public Monster(Game game, Position position, int level, int lives) {
        super(game, position, level, lives);
        timeSinceLastMove = System.nanoTime();
    }

    @Override
    public void update(long now) {
        if (getLives() <= 0) this.remove();
        if (System.nanoTime() > timeSinceLastMove + baseVelocity / game.monsterVelocity) {
            requestMove(Direction.random());
            timeSinceLastMove = System.nanoTime();
        }
        super.update(now);

    }

}

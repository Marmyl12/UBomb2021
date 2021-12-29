/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.entity.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.PathFinder;
import fr.ubx.poo.ubomb.game.Position;

public class Monster extends Character {

    private long timeSinceLastMove;
    private final long baseVelocity = 10_000_000_000L;

    public Monster(Game game, Position position, int level, int lives) {
        super(game, position, level, lives, game.monsterInvisibilityTime);
        timeSinceLastMove = System.nanoTime();
    }

    @Override
    public void update(long now) {
        if (getLives() <= 0) this.remove();
        // Move the monster depending on its velocity
        if (System.nanoTime() > timeSinceLastMove + baseVelocity / (game.monsterVelocity + getLevel() * 5L)) {
            Direction nextMove = null;
            // Compute the next move using the A* algorithm only if the player is on the last level and is not invisible  (to give him time to breathe)
            if (getLevel() == game.getCurrentLevel() && getLevel() == game.levels - 1 && !game.getPlayer().isInvincible())
                nextMove = PathFinder.nextBestMove(getPosition(), game.getPlayer().getPosition(), this);
            if (nextMove == null)
                requestMove(Direction.random());
            else
                requestMove(nextMove);
            timeSinceLastMove = System.nanoTime();
        }
        super.update(now);
    }

    @Override
    public boolean isWalkable(Character character) {
        return character instanceof Player;
    }
}



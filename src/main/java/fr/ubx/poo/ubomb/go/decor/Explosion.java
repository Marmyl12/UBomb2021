package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.Entity;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Character;

import java.util.List;

public class Explosion extends Entity {

    private final long spawnTime;
    private final float timeToLive = 0.5f;

    public Explosion(Game game, Position position, int level) {
        super(game, position, level);
        spawnTime = System.nanoTime();
    }

    @Override
    public boolean isWalkable(Character character) {
        return true;
    }

    @Override
    public void update(long now) {
        game.getGameObjects(getPosition(), getLevel()).forEach(GameObject::explode);
        Decor decor = game.getGrid(getLevel()).get(getPosition());
        if (decor != null)
            decor.explode();
        if (System.nanoTime() > spawnTime + timeToLive * 1_000_000_000) this.remove();
    }
}

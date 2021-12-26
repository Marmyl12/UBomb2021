package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Character;

public class Explosion extends Decor {

    private final long spawnTime;
    private final int timeToLive = 1;

    public Explosion(Position position) {
        super(position);
        spawnTime = System.nanoTime();
    }

    @Override
    public boolean isWalkable(Character character) {
        return true;
    }

    public void update() {
        if (System.nanoTime() > spawnTime + timeToLive * 1_000_000_000) this.remove();
    }
}

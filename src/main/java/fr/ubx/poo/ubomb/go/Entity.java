package fr.ubx.poo.ubomb.go;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;

public abstract class Entity extends GameObject {

    private int level;

    public Entity(Game game, Position position, int level) {
        super(game, position);
        this.level = level;
    }

    public abstract void update (long now);

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}

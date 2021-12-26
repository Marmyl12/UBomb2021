package fr.ubx.poo.ubomb.go;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Character;
import fr.ubx.poo.ubomb.go.character.Player;

public class Bomb extends GameObject {
    private final long timeSinceCreation;
    private int currentPhase;

    public Bomb(Position position) {
        super(position);
        this.timeSinceCreation = System.nanoTime();
        this.currentPhase = getPhase();
    }

    @Override
    public boolean isWalkable(Character character) { return true; }

    //return the "phase" of the bomb (0 - 3 : string burning, 4 exploding)
    public int getPhase() {
        int phase = (int) ((System.nanoTime() - timeSinceCreation) / 1_000_000_000);
        if (phase > 3) return 3;
        return phase;
    }

    public boolean hasPhaseChanged() {
        if (currentPhase != getPhase()) {
            currentPhase = getPhase();
            return true;
        }
        return false;
    }

    public boolean mustExplode() {
        return (System.nanoTime() - timeSinceCreation) / 1_000_000_000 > 3;
    }

}

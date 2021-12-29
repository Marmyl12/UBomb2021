package fr.ubx.poo.ubomb.go;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Character;
import fr.ubx.poo.ubomb.go.character.Player;

public class Bomb extends Entity {
    private final long timeSinceCreation;
    private int currentPhase;
    private boolean mustExplode;
    public final int range;

    public Bomb(Game game, Position position, int level) {
        super(game, position, level);
        this.timeSinceCreation = System.nanoTime();
        this.currentPhase = getPhase();
        mustExplode = false;
        range = game.getPlayer().getBombRange();
    }

    @Override
    public boolean isWalkable(Character character) { return character instanceof Player; }

    @Override
    public void update(long now) {
        if ((System.nanoTime() - timeSinceCreation) / 1_000_000_000 > 3) mustExplode = true;
    }

    @Override
    public void explode() {
        mustExplode = true;
    }

    //return the "phase" of the bomb (0 - 3 : string burning, 4 exploding)
    public int getPhase() {
        int phase = (int) ((System.nanoTime() - timeSinceCreation) / 1_000_000_000);
        return Math.min(phase, 3);
    }

    public boolean hasPhaseChanged() {
        if (currentPhase != getPhase()) {
            currentPhase = getPhase();
            return true;
        }
        return false;
    }

    public boolean mustExplode() {
        return mustExplode;
    }

}

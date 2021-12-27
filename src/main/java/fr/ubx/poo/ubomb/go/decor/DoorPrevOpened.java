package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.Takeable;
import fr.ubx.poo.ubomb.go.character.Character;
import fr.ubx.poo.ubomb.go.character.Player;

public class DoorPrevOpened extends Decor implements Takeable {
    public DoorPrevOpened(Position position) { super(position); }

    @Override
    public boolean isWalkable(Character character) { return character instanceof Player; }

    @Override
    public void takenBy(Player player) {
        Game game = player.game;
        game.setChangeLevel(true);
        game.setCurrentLevel(game.getCurrentLevel() - 1);
        game.setCurrentVelocity(game.getCurrentLevel()-1);
        game.getPlayer().setPosition(game.getGrid().getEndPos());
    }
}

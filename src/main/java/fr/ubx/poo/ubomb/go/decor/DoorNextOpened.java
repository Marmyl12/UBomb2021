package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.Takeable;
import fr.ubx.poo.ubomb.go.character.Character;
import fr.ubx.poo.ubomb.go.character.Player;

public class DoorNextOpened extends Decor implements Takeable {
    public DoorNextOpened(Position position) { super(position); }

    @Override
    public boolean isWalkable(Character character) { return true; }

    @Override
    public void takenBy(Player player) {
        Game game = player.game;
        game.setChangeLevel(true);
        game.setCurrentLevel(game.getCurrentLevel()+1);
        player.setPosition(game.getGrid().getStartPos());
    }
}

package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class DoorNextOpened extends Bonus {
    public DoorNextOpened(Position position) {
        super(position);
    }

    @Override
    public boolean isWalkable(Player player) {
        return true;
    }

    public void takenBy(Player player) {
        // à changer pour que ça corresponde a l'etage actuelle plus 1
        player.takeDoor(2);
    }
}

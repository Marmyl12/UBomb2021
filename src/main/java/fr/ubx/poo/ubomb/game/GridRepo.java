package fr.ubx.poo.ubomb.game;

import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.*;


public abstract class GridRepo {

    private final Game game;

    GridRepo(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public abstract Grid load(int level, String name);

    GameObject processEntityCode(EntityCode entityCode, Position pos) {
        switch (entityCode) {
            case Empty:
                return null;
            case Stone:
                return new Stone(pos);
            case Tree:
                return new Tree(pos);
            case Key:
                return new Key(pos);
            case Box:
                return new Box(pos);
            case Heart:
                return new Heart(pos);
            case BombRangeInc:
                return new BombRangeInc(pos);
            case BombRangeDec:
                return new BombRangeDec(pos);
            case BombNumberInc:
                return new BombNbInc(pos);
            case BombNumberDec:
                return new BombNbDec(pos);
            case Princess:
                return new Princess(pos);
            case Monster:
                return new Monster(game, pos, 1);
            case DoorNextClosed:
                return new DoorNextClosed(pos);
            case DoorNextOpened:
                return new DoorNextOpened(pos);
            case DoorPrevOpened:
                return new DoorPrevOpened(pos);
            default:
                return null;
                // throw new RuntimeException("EntityCode " + entityCode.name() + " not processed");
        }
    }
}

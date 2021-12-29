package fr.ubx.poo.ubomb.game;

import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.entity.character.Monster;
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

    GameObject processEntityCode(EntityCode entityCode, Position pos, int level) {
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
                return new Box(game, pos);
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
                return new Monster(game, pos, level, 1 + (level + 1) / 2);
            case DoorNextClosed:
                return new DoorNextClosed(pos);
            case DoorNextOpened:
                return new DoorNextOpened(pos);
            case DoorPrevOpened:
                return new DoorPrevOpened(pos);
            default:
                throw new RuntimeException("EntityCode " + entityCode.name() + " not processed");
        }
    }
}

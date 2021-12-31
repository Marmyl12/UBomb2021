/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.entity.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.Takeable;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.Bonus;
import fr.ubx.poo.ubomb.go.entity.Entity;

import java.util.List;


public class Player extends Character {

    private static final int maxRange = 9;
    private static final int maxCapacity = 9;
    private static final int maxLives = 9;

    private int bombBagCapacity;
    private int bombRange;
    private int keys;
    private int placedBombs;

    public Player(Game game, Position position, int lives, int bombBagCapacity, int bombRange) {
        super(game, position, game.getCurrentLevel(), lives, game.playerInvisibilityTime);
        this.bombBagCapacity = bombBagCapacity;
        this.bombRange = bombRange;
        this.keys = 0;
        placedBombs = 0;
    }

    @Override
    public boolean canMove(Direction direction) {
        if (super.canMove(direction)) return true;
        Decor decor = game.getGrid().get(direction.nextPosition(getPosition()));
        if (decor instanceof Box) {
            return ((Box) decor).canMove(direction);
        }
        return false;
    }

    @Override
    public void doMove(Direction direction) {
        super.doMove(direction);
        Decor go = game.getGrid().get(getPosition());
        if (go instanceof Takeable) {
            ((Takeable) go).takenBy(this);
            if (go instanceof Bonus) go.remove();
        } else if (go instanceof Box) {
            ((Box) go).doMove(direction);
        }

    }

    //return true if the player can place a bomb and increase placed bombs
    public boolean useBomb() {
        List<Entity> list = game.getGameObjects(getPosition());
        for (Entity e : list) {
            if (e instanceof Monster ) return false;
            }
        if (placedBombs < bombBagCapacity) {
            placedBombs++;
            return true;
        }


        return false;
    }

    public void retrieveBomb() {
        placedBombs--;
    }

    public void takeHeart() {
        if (getLives() < maxLives) setLives(getLives()+1);
    }
    public void takeKey() { keys++; }
    public void takeBombRangeInc() { if (bombRange < maxRange) bombRange++; }
    public void takeBombRangeDec() { if (bombRange > 0) bombRange--; }
    public void BombNumberInc() { if (bombBagCapacity < maxCapacity) bombBagCapacity++; }
    public void BombNumberDec() { if (bombBagCapacity > 1) bombBagCapacity--; }

    // return true and use a key if the decor in front of the player is a door and it can be opened
    public boolean openDoor() {
        Position position = direction.nextPosition(getPosition());
        Decor go = game.getGrid().get(position);
        if (go instanceof DoorNextClosed && keys > 0) {
            keys--;
            return true;
        }
        return false;
    }

    public int getBombRange() {
        return bombRange;
    }

    public int getKeys() {
        return keys;
    }

    public int getAvailableBombs() {
        return bombBagCapacity - placedBombs;
    }

    public boolean isWinner() {
        return game.getGrid().get(getPosition()) instanceof Princess;
    }
}

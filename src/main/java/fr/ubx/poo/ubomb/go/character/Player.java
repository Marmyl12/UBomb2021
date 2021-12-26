/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Takeable;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.Bonus;


public class Player extends Character {

    private int bombBagCapacity;
    private int bombRange;
    private int keys;
    private long lastTimeDamaged;
    private int placedBombs;

    public Player(Game game, Position position, int lives, int bombBagCapacity, int bombRange) {
        super(game, position, lives);
        this.bombBagCapacity = bombBagCapacity;
        this.bombRange = bombRange;
        this.keys = 0;
        lastTimeDamaged = System.nanoTime();
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

    @Override
    public void explode() {
    }

    //return true if the player can place a bomb and increase placed bombs
    public boolean useBomb() {
        if (placedBombs < bombBagCapacity) {
            placedBombs++;
            return true;
        }
        return false;
    }

    // Example of methods to define by the player
    public void takeDoor(int gotoLevel) {}
    public void takeHeart() {
        setLives(getLives()+1);
    }
    public void takeKey() { keys++; }
    public void takeBombRangeInc() { bombRange++; }
    public void takeBombRangeDec() { bombRange--; }
    public void BombNumberInc() { bombBagCapacity++; }
    public void BombNumberDec() { bombBagCapacity--; }

    public boolean openDoor() {
        Position position = direction.nextPosition(getPosition());
        Decor go = game.getGrid().get(position);
        if (go instanceof DoorNextClosed && keys > 0) {
            keys--;
            return true;
        }
        return false;
    }

    public void takeDamage() {
        loseLive();
        lastTimeDamaged = System.nanoTime();
    }

    //Return true if the player has been hit since less than 1 second (1 billion nanoseconds)
    public boolean isInvincible() {
        return lastTimeDamaged + 1_000_000_000 > System.nanoTime();
    }

    public int getBombBagCapacity() {
        return bombBagCapacity;
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

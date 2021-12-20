/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.Bonus;


public class Player extends Character {

    private int bombBagCapacity;
    private int availableBombs;
    private int bombRange;
    private int keys;
    private long lastTimeDamaged;

    public Player(Game game, Position position, int lives, int bombBagCapacity, int bombRange) {
        super(game, position, lives);
        this.bombBagCapacity = bombBagCapacity;
        availableBombs = bombBagCapacity;
        this.bombRange = bombRange;
        this.keys = 0;
        lastTimeDamaged = System.nanoTime();
    }

    @Override
    public void doMove(Direction direction) {
        super.doMove(direction);
        Decor go = game.getGrid().get(getPosition());
        if (go instanceof Bonus) {
            ((Bonus) go).takenBy(this);
            go.remove();
        }

    }

    @Override
    public void explode() {
    }

    public boolean pushBox() {
        Position Pos = direction.nextPosition(getPosition());
        Position nextn = direction.nextPosition(Pos);
        Decor d1 = game.getGrid().get(Pos);
        Decor d2 = game.getGrid().get(nextn);
        return (d1 instanceof Box) && d2 == null;
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
        return availableBombs;
    }

    public void useBomb() { availableBombs--; }

    public boolean isWinner() {
        return game.getGrid().get(getPosition()) instanceof Princess;
    }
}

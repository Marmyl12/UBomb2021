/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.Bonus;


public class Player extends GameObject implements Movable {

    public Direction direction;
    private boolean moveRequested = false;
    private int lives;
    private int bombBagCapacity;
    private int availableBombs;
    private int bombRange;
    private int keys;

    public Player(Game game, Position position, int lives, int bombBagCapacity, int bombRange) {
        super(game, position);
        this.direction = Direction.DOWN;
        this.lives = lives;
        this.bombBagCapacity = bombBagCapacity;
        availableBombs = bombBagCapacity;
        this.bombRange = bombRange;
        this.keys = 0;
    }

    public int getLives() {
        return lives;
    }

    public void loseLive(int lives) { this.lives -= lives; }

    public void loseLive() { loseLive(1); }

    public Direction getDirection() {
        return direction;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
            setModified(true);
        }
        moveRequested = true;
    }

    public final boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        GameObject obj = game.getGrid().get(nextPos);
        //Check collision with obstacle
        if (obj != null) {
            return obj.isWalkable(this);
        }
        int height = game.getGrid().getHeight();
        int width = game.getGrid().getWidth();
        //Check collision with the grid
        if (nextPos.getX() < 0 || nextPos.getY() < 0 || nextPos.getX() >= width || nextPos.getY() >= height) return false;
        return true;
    }



    public void update(long now) {
        if (moveRequested) {
            if (game.getGrid().get(direction.nextPosition(getPosition())) instanceof Monster) loseLive();
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }

    public void doMove(Direction direction) {
        // Check if we need to pick something up
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
        Decor go = game.getGrid().get(nextPos);
        if (go instanceof Bonus) {
            ((Bonus) go).takenBy(this);
            go.remove();
        }
    }

    @Override
    public boolean isWalkable(Player player) {
        return true;
    }

    @Override
    public void explode() {
    }

    // Example of methods to define by the player
    public void takeDoor(int gotoLevel) {}
    public void takeKey() { keys++; }
    public void takeHeart() {}
    public void takeBombRangeInc() {}
    public void takeBombRangeDec() {}
    public void BombNumberInc() {}
    public void BombNumberDec() {}

    public boolean openDoor() {
        Position position = direction.nextPosition(getPosition());
        Decor go = game.getGrid().get(position);
        if (go instanceof DoorNextClosed && keys > 0) {
            keys--;
            return true;
        }
        return false;
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

    public boolean isWinner() {
        return game.getGrid().get(getPosition()) instanceof Princess;
    }
}

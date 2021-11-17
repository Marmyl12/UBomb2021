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
        Position nextn = direction.nextPosition(nextPos);
        Position nextn2 = direction.nextPosition(nextn);
        setPosition(nextPos);
        Decor go = game.getGrid().get(nextPos);
        Decor Nextdec = game.getGrid().get(nextn);
        Decor Next2 = game.getGrid().get(nextn2);
        if (go instanceof Bonus) {
            ((Bonus) go).takenBy(this);
            go.remove();
        }
        if ((Nextdec instanceof Box)&& Next2==null) {
            Nextdec.remove();
        }
    }

    @Override
    public boolean isWalkable(Player player) {
        return true;
    }

    @Override
    public void explode() {
    }

    public void pushBox() {

    }
    // Example of methods to define by the player
    public void takeDoor(int gotoLevel) {}
    public void takeHeart() {
        this.lives+=1;
    }
    public void takeKey() {this.keys+=1;}
    public void takeBombRangeInc() {this.bombRange+=1;}
    public void takeBombRangeDec() {this.bombRange-=1;}
    public void BombNumberInc() {this.bombBagCapacity+=1;}
    public void BombNumberDec() {this.bombBagCapacity-=1;}

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

package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.Entity;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.decor.Decor;

public abstract class Character extends Entity implements Movable {

    public Direction direction;
    private boolean moveRequested = false;
    private int lives;
    private long lastTimeDamaged;

    public Character(Game game, Position position, int level, int lives) {
        super(game, position, level);
        this.direction = Direction.DOWN;
        this.lives = lives;
        lastTimeDamaged = System.nanoTime();
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) { this.lives = lives; }

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

    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        GameObject obj = game.getGrid(getLevel()).get(nextPos);
        //Check collision with obstacle
        if (obj != null) {
            return obj.isWalkable(this);
        }
        //Check collision with the grid
        return game.inside(nextPos, getLevel());
    }

    @Override
    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    public void takeDamage() {
        loseLive();
        lastTimeDamaged = System.nanoTime();
    }

    //Return true if the character has been hit since less than 1 second (1 billion nanoseconds)
    public boolean isInvincible() {
        return lastTimeDamaged + 1_000_000_000 > System.nanoTime();
    }

    @Override
    public boolean isWalkable(Character character) {
        return true;
    }

    @Override
    public void explode() {
        if (!isInvincible())
            this.takeDamage();
    }

}

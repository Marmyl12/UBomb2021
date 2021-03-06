package fr.ubx.poo.ubomb.go.entity.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.entity.Entity;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;

import java.util.List;

public abstract class Character extends Entity implements Movable {

    public Direction direction;
    private boolean moveRequested = false;
    private int lives;
    private long lastTimeDamaged;
    private final long invisibilityTime;

    public Character(Game game, Position position, int level, int lives, long invisibilityTime) {
        super(game, position, level);
        this.direction = Direction.DOWN;
        this.lives = lives;
        lastTimeDamaged = 0;
        this.invisibilityTime = invisibilityTime;
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
        //Check collision with entities
        List<Entity> entities = game.getGameObjects(direction.nextPosition(getPosition()), getLevel());
        for (Entity entity : entities)
            if (!entity.isWalkable(this))
                return false;
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

    // Return true if the character is still invincible
    public boolean isInvincible() {
        return lastTimeDamaged + invisibilityTime * 1_000_000L > System.nanoTime();
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

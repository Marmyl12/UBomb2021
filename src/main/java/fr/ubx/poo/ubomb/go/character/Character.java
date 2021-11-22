package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.decor.Decor;

public abstract class Character extends GameObject implements Movable {
    public Direction direction;
    private boolean moveRequested = false;
    private int lives;

    public Character(Game game, Position position, int lives) {
        super(game, position);
        this.direction = Direction.DOWN;
        this.lives = lives;
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
        GameObject obj = game.getGrid().get(nextPos);
        //Check collision with obstacle
        if (obj != null) {
            return obj.isWalkable(this);
        }
        int height = game.getGrid().getHeight();
        int width = game.getGrid().getWidth();
        //Check collision with the grid
        return nextPos.getX() >= 0 && nextPos.getY() >= 0 && nextPos.getX() < width && nextPos.getY() < height;
    }

    public void update(long now) {
        if (moveRequested) {
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
    }

    @Override
    public boolean isWalkable(Character character) {
        return true;
    }

}

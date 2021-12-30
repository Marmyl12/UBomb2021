/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.engine;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.decor.bonus.Bonus;
import fr.ubx.poo.ubomb.go.entity.Bomb;
import fr.ubx.poo.ubomb.go.entity.Entity;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.entity.Explosion;
import fr.ubx.poo.ubomb.go.entity.character.Monster;
import fr.ubx.poo.ubomb.go.entity.character.Player;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.view.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.*;


public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    private final List<Sprite> sprites = new LinkedList<>();
    private final Set<Sprite> cleanUpSprites = new HashSet<>();
    private final Stage stage;
    private StatusBar statusBar;
    private Pane layer;
    private Input input;


    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.stage = stage;
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        initialize();
        buildAndSetGameLoop();
    }

    private void initialize() {
        Group root = new Group();
        layer = new Pane();

        int height = game.getGrid().getHeight();
        int width = game.getGrid().getWidth();
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;
        double scale = (float) (sceneWidth) / ((float) (12 * Sprite.size));
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height * scale);
        Rectangle bg = new Rectangle(scene.getWidth(), sceneHeight);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
        if (game.getCurrentLevel()==0) {
            Image img = ImageResource.BACKGROUND1.getImage();
                    ImagePattern pat = new ImagePattern(img);
                    bg.setFill(pat);
        }
        if (game.getCurrentLevel()==1) {
            Image img = ImageResource.BACKGROUND2.getImage();
            ImagePattern pat = new ImagePattern(img);
            bg.setFill(pat);
        }
        if (game.getCurrentLevel()==2) {
            Image img = ImageResource.BACKGROUND3.getImage();
            ImagePattern pat = new ImagePattern(img);
            bg.setFill(pat);
        }
        root.getChildren().add(bg);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);

        // Create sprites
        for (Decor decor : game.getGrid().values()) {
            sprites.add(SpriteFactory.create(layer, decor));
            decor.setModified(true);
        }
        for (Entity entity : game.getGrid().getEntities()) {
            sprites.add(SpriteFactory.create(layer, entity));
            entity.setModified(true);
        }
        sprites.add(new SpritePlayer(layer, player));
    }

    void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);

                // Do actions
                update(now);
                checkCollision(now);
                checkExplosions();

                // Graphic update
                cleanupSprites();
                render();
                statusBar.update(game);
            }
        };
    }

    // Remove bombes and create explosions and their sprites
    private void checkExplosions() {
        for (int i = 0 ; i < game.levels ; i++) {
            List<Explosion> newExplosions = null;
            for (Entity entity : game.getGrid(i).getEntities()) {
                if (entity instanceof Bomb) {
                    Bomb bomb = (Bomb) entity;
                    if (bomb.mustExplode()) {
                        newExplosions = explodeBomb(bomb, i);
                        player.retrieveBomb();
                    }
                }
            }
            if (newExplosions != null) {
                game.getGrid(i).getEntities().addAll(newExplosions);
                if (i == game.getCurrentLevel())
                    newExplosions.forEach(explosion -> sprites.add(SpriteFactory.create(layer, explosion)));
            }
        }
    }

    //Explode a bomb, setup sprites and return a list of explosions to add to entities
    private List<Explosion> explodeBomb(Bomb bomb, int level) {
        bomb.remove();
        List<Explosion> explosions = new LinkedList<>();
        Explosion explosion = new Explosion(game, bomb.getPosition(), level);
        explosions.add(explosion);
        for (Direction direction : Direction.values()) {
            boolean boxDestroyed = false;
            for (int i = 0; i < bomb.range ; i++) {
                Position position = direction.nextPosition(bomb.getPosition(), i + 1);
                if (!game.inside(position, level)) break;
                Decor decor = game.getGrid(level).get(position);
                if (decor != null && !(decor instanceof Bonus)) {
                     if (decor instanceof Box && !boxDestroyed) {
                        boxDestroyed = true;
                     } else {
                        break;
                    }
                }
                Explosion sideExplosion = new Explosion(game, position, level);
                explosions.add(sideExplosion);
            }
        }
        return explosions;
    }

    // Check if the player collides with monsters
    private void checkCollision(long now) {
        if (!player.isInvincible()) {
            List<Entity> gos = game.getGameObjects(player.getPosition());
            for (GameObject go : gos) {
                if (go instanceof Monster) {
                    player.takeDamage();
                }
            }
        }
    }

    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        } else if (input.isMoveDown()) {
            player.requestMove(Direction.DOWN);
        } else if (input.isMoveLeft()) {
            player.requestMove(Direction.LEFT);
        } else if (input.isMoveRight()) {
            player.requestMove(Direction.RIGHT);
        } else if (input.isMoveUp()) {
            player.requestMove(Direction.UP);
        } else if (input.isBomb() && player.useBomb()) {
            Bomb bomb = new Bomb(game, player.getPosition(), game.getCurrentLevel());
            game.getGrid().addEntity(bomb);
            sprites.add(new SpriteBomb(layer, bomb));
        } else if (input.isKey()) {
            openDoor();
        }
        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }


    private void update(long now) {
        player.update(now);
        for (int i = 0 ; i < game.levels ; i++) {
            List<Entity> entitiesToRemove = new LinkedList<>();
            for (Entity entity : game.getGrid(i).getEntities()) {
                entity.update(now);
                if (entity.isDeleted() && game.getCurrentLevel() != i) {
                    // Remove destroyed entities from unloaded levels
                    entitiesToRemove.add(entity);
                }
            }
            game.getGrid(i).getEntities().removeAll(entitiesToRemove);
        }

        if (player.getLives() == 0) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }

        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Gagné", Color.BLUE);
        }

        if (game.isChangeLevel()) {
            gameLoop.stop();
            loadLevel();
            gameLoop.start();
        }

    }

    public void cleanupSprites() {
        sprites.forEach(sprite -> {
            GameObject gameObject = sprite.getGameObject();
            if (gameObject.isDeleted()) {
                if (gameObject instanceof Decor)
                    game.getGrid().remove(sprite.getPosition());
                else
                    game.getGrid().getEntities().remove(gameObject);
                cleanUpSprites.add(sprite);
            }
        });
        cleanUpSprites.forEach(Sprite::remove);
        sprites.removeAll(cleanUpSprites);
        cleanUpSprites.clear();
    }

    private void render() {
        sprites.forEach(sprite -> {
            sprite.updateImage();
            sprite.render();
        });
    }

    public void start() {
        gameLoop.start();
    }

    public void openDoor() {
        if (player.openDoor()) {
            Position position = player.getDirection().nextPosition(player.getPosition());
            game.getGrid().get(position).remove();
            cleanupSprites();
            game.getGrid().set(position, new DoorNextOpened(position));
            sprites.add(SpriteFactory.create(layer, game.getGrid().get(position)));
        }
    }

    public void loadLevel() {
        cleanUpSprites.addAll(sprites);
        cleanupSprites();
        initialize();
        game.setChangeLevel(false);
    }
}

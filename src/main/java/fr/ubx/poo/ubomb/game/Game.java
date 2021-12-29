/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.game;


import fr.ubx.poo.ubomb.go.entity.Entity;
import fr.ubx.poo.ubomb.go.entity.character.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class Game {

    public final int bombBagCapacity;
    public final int monsterVelocity;
    public final int playerLives;
    public final int levels;
    public final long playerInvisibilityTime;
    public final long monsterInvisibilityTime;
    private final List<Grid> grids = new LinkedList<>();
    private final Player player;
    private int currentLevel;
    private boolean changeLevel;

    public Game(String worldPath) {
        try (InputStream input = new FileInputStream(new File(worldPath, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            bombBagCapacity = Integer.parseInt(prop.getProperty("bombBagCapacity", "3"));
            monsterVelocity = Integer.parseInt(prop.getProperty("monsterVelocity", "10"));
            levels = Integer.parseInt(prop.getProperty("levels", "1"));
            playerLives = Integer.parseInt(prop.getProperty("playerLives", "3"));
            playerInvisibilityTime = Long.parseLong(prop.getProperty("playerInvisibilityTime", "4000"));
            monsterInvisibilityTime = Long.parseLong(prop.getProperty("monsterInvisibilityTime", "1000"));

            // Load the world
            String prefix = prop.getProperty("prefix");
            GridRepo gridRepo = new GridRepoFile(this, worldPath);
            for (int i = 0 ; i < levels ; i++) {
                this.grids.add(gridRepo.load(i, prefix));
            }
            currentLevel = 0;

            // Create the player
            String[] tokens = prop.getProperty("player").split("[ :x]+");
            if (tokens.length != 2)
                throw new RuntimeException("Invalid configuration format");
            Position playerPosition = new Position(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
            player = new Player(this, playerPosition, playerLives, bombBagCapacity, 1);

            changeLevel = false;

        } catch (IOException ex) {
            System.err.println("Error loading configuration");
            throw new RuntimeException("Invalid configuration format");
        }
    }

    public Grid getGrid(int level) {
        return grids.get(level);
    }

    public Grid getGrid() {
        return getGrid(currentLevel);
    }

    public boolean isChangeLevel() {
        return changeLevel;
    }

    public void setChangeLevel(boolean changeLevel) {
        this.changeLevel = changeLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    // Returns all the entities at a given position on the current level
    public List<Entity> getGameObjects(Position position) {
        return getGameObjects(position, getCurrentLevel());
    }

    // Returns all the entities at a given position and a given level
    public List<Entity> getGameObjects(Position position, int level) {
        List<Entity> gos = new LinkedList<>();
        if (getPlayer().getPosition().equals(position))
            gos.add(player);
        for (Entity entity : getGrid(level).getEntities()) {
            if (entity.getPosition().equals(position))
                gos.add(entity);
        }
        return gos;
    }

    public Player getPlayer() {
        return this.player;
    }

    // Returns true if the given position is inside the given level
    public boolean inside(Position position, int level) {
        return position.getX() >= 0 && position.getY() >= 0 && position.getX() < getGrid(level).getWidth() && position.getY() < getGrid(level).getHeight();
    }

    // Returns true if the given position is inside the current level
    public boolean inside(Position position) {
        return inside(position, getCurrentLevel());
    }

}

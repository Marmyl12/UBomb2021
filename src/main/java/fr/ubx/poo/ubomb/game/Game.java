/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.game;


import fr.ubx.poo.ubomb.go.Bomb;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.character.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class Game {

    public final int bombBagCapacity;
    public int monsterVelocity;
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
                this.grids.add(gridRepo.load(i+1, prefix));
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

    public void setCurrentVelocity(int currentLevel) {
        if(currentLevel==2) monsterVelocity=15;
        if(currentLevel==3) monsterVelocity=20;

    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    // Returns the player, monsters and bombs at a given position
    public List<GameObject> getGameObjects(Position position) {
        List<GameObject> gos = new LinkedList<>();
        if (getPlayer().getPosition().equals(position))
            gos.add(player);
        for (GameObject entity : getGrid().getEntities()) {
            if (entity.getPosition().equals(position))
                gos.add(entity);
        }
        return gos;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean inside(Position position) {
        return position.getX() >= 0 && position.getY() >= 0 && position.getX() < getGrid().getWidth() && position.getY() < getGrid().getHeight();
    }

}

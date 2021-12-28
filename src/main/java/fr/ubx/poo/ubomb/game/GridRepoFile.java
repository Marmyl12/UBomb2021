package fr.ubx.poo.ubomb.game;

import fr.ubx.poo.ubomb.go.Entity;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.decor.Decor;
import fr.ubx.poo.ubomb.go.decor.DoorNextClosed;
import fr.ubx.poo.ubomb.go.decor.DoorNextOpened;
import fr.ubx.poo.ubomb.go.decor.DoorPrevOpened;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GridRepoFile extends GridRepo {

    private String worldPath;

    public GridRepoFile(Game game, String worldPath) {
        super(game);
        this.worldPath = worldPath;
    }

    @Override
    public Grid load(int level, String name) {
        try {
            File file = new File(worldPath, name + (level + 1) + ".txt");
            FileReader reader = new FileReader(file);
            int width, height = 0, amount = 0;
            int c;
            while ((c = reader.read()) != -1) {
                if (c == '\n') {
                    height++;
                } else {
                    amount++;
                }
            }
            width = amount / height;
            Grid grid = new Grid(width, height);
            reader = new FileReader(file);
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    Position position = new Position(i, j);
                    c = reader.read();
                    EntityCode entityCode = EntityCode.fromCode((char) c);
                    GameObject go = processEntityCode(entityCode, position, level);
                    if (go instanceof Decor) {
                        if (go instanceof DoorPrevOpened) {
                            grid.setStartPos(position);
                        } else if (go instanceof DoorNextOpened || go instanceof DoorNextClosed) {
                            grid.setEndPos(position);
                        }
                        grid.set(position, (Decor) go);
                    } else if (go instanceof Monster) {
                        grid.addEntity((Entity) go);
                    }
                }
                reader.read();
            }
            return grid;
        } catch (IOException e) {
            System.err.println(e);
            throw new GridRepoException("Error while loading grid");
        }
    }
}

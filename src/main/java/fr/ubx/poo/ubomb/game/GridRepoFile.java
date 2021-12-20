package fr.ubx.poo.ubomb.game;

import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.decor.Decor;

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
            File file = new File(worldPath, name + ".txt");
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
                    System.out.println((char) c);
                    EntityCode entityCode = EntityCode.fromCode((char) c);
                    GameObject go = processEntityCode(entityCode, position);
                    if (go instanceof Decor) {
                        grid.set(position, (Decor) go);
                    } else if (go instanceof Monster) {
                        getGame().addMonster((Monster) go);
                    }
                }
                reader.read();
            }
            return grid;
        } catch (IOException e) {
            System.out.println("Error while loading grid");
            System.out.println(e);
        }
        return null;
    }
}

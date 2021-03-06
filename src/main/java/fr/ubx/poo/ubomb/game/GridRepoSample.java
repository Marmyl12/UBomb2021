package fr.ubx.poo.ubomb.game;

import fr.ubx.poo.ubomb.go.entity.Entity;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.entity.character.Monster;
import fr.ubx.poo.ubomb.go.decor.Decor;

import java.lang.reflect.Field;

import static fr.ubx.poo.ubomb.game.EntityCode.*;

public class GridRepoSample extends GridRepo {

    private final EntityCode[][] level1 = {
            {Stone, Heart, Heart, Empty, Empty, Empty, Empty, Empty, Empty, Empty, BombRangeDec, Heart},
            {Empty, Stone, Stone, Empty, Stone, Empty, Stone, Stone, Stone, Stone, Empty, Empty},
            {Empty, Empty, Empty, Empty, Stone, Box, Stone, Empty, Empty, Stone, Empty, Empty},
            {Empty, Empty, Empty, Empty, Stone, Box, Stone, Empty, Empty, Stone, Empty, Empty},
            {Empty, Box, Empty, Empty, Stone, Stone, Stone, Empty, Empty, Empty, Empty, Empty},
            {Empty, Empty, Empty, Empty, Empty, Box, Empty, Key, Empty, Stone, Empty, Empty},
            {Empty, Tree, Empty, Tree, Empty, Empty, Empty, Empty, Empty, Stone, Empty, Empty},
            {Empty, Empty, Box, Tree, Empty, Empty, Empty, Empty, Empty, Stone, DoorNextClosed, Empty},
            {Empty, Tree, Tree, Tree, Empty, Empty, Empty, Empty, Empty, Stone, Empty, Empty},
            {Empty, Empty, Empty, Empty, Empty, Empty, BombRangeInc, Empty, Empty, Empty, Empty, Empty},
            {Stone, Stone, Stone, Stone, Stone, Empty, Box, Box, Stone, Stone, Box, Stone},
            {Empty, Heart, Empty, Empty, Box, Empty, Empty, Empty, Empty, Empty, Princess, Empty},
            {Monster, Empty, Empty, Empty, Box, Empty, Empty, Empty, BombNumberInc, Empty, Empty, Monster}
    };

    public GridRepoSample(Game game) {
        super(game);
    }

    @Override
    public final Grid load(int level, String name) {
        EntityCode[][] entities = getEntities(name);
        if (entities == null)
            return null;
        int width = entities[0].length;
        int height = entities.length;
        Grid grid = new Grid(width, height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Position position = new Position(i, j);
                EntityCode entityCode = entities[j][i];
                GameObject go = processEntityCode(entityCode, position, level);
                if (go instanceof Decor) {
                    grid.set(position, (Decor) go);
                } else if (go instanceof Monster) {
                    grid.addEntity((Entity) go);
                }
            }
        }

        return grid;
    }

    private final EntityCode[][] getEntities(String name) {
        try {
            Field field = this.getClass().getDeclaredField(name);
            return (EntityCode[][]) field.get(this);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return null;
        }
    }
}

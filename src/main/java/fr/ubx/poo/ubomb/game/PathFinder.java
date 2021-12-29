package fr.ubx.poo.ubomb.game;


import fr.ubx.poo.ubomb.go.entity.Entity;
import fr.ubx.poo.ubomb.go.entity.character.Monster;
import fr.ubx.poo.ubomb.go.decor.Decor;
import fr.ubx.poo.ubomb.go.entity.Explosion;

import java.util.*;

public class PathFinder {

    private static class PositionNode {

        private final Position position;
        private PositionNode parent;
        private int g;
        private int f;

        public PositionNode(Position position) {
            this.position = position;
            parent = null;
        }

    }
    // Return the best direction from a to b for character based on the A* pathfinding algorithm
    public static Direction nextBestMove(Position a, Position b, Monster monster) {
        Set<PositionNode> openList = new HashSet<>();
        Set<PositionNode> closedList = new HashSet<>();
        PositionNode start = new PositionNode(a);
        openList.add(start);
        start.g = 0;
        start.f = start.g + Position.manhattan(start.position, b);
        while (!openList.isEmpty()) {
            PositionNode current = bestFScore(openList);
            if (current.position.equals(b)) {
                return findDirection(a, getNextPosition(current));
            }
            openList.remove(current);
            closedList.add(current);
            for (PositionNode neighbor : neighbors(current, monster)) {
                if (getSamePosition(neighbor, closedList) == null) {
                    neighbor.f = neighbor.g + Position.manhattan(neighbor.position, b);
                    PositionNode openNeighbor = getSamePosition(neighbor, openList);
                    if (openNeighbor == null) {
                        openList.add(neighbor);
                    }
                    else {
                        if (neighbor.g < openNeighbor.g) {
                            openNeighbor.g = neighbor.g;
                            openNeighbor.parent = neighbor.parent;
                        }
                    }
                }
            }
        }
        return null;
    }

    // return the element with the best F Score in the set
    private static PositionNode bestFScore(Set<PositionNode> openList) {
        int max = -1;
        PositionNode ret = null;
        for (PositionNode positionNode : openList) {
            if (positionNode.f < max || max == -1) {
                ret = positionNode;
                max = positionNode.f;
            }
        }
        return ret;
    }

    // return all of the neighbors of the given position walkable by the monster
    private static Set<PositionNode> neighbors(PositionNode position, Monster monster) {
        Set<PositionNode> neighborList = new HashSet<>();
        for (Direction direction : Direction.values()) {
            Position neighbor = direction.nextPosition(position.position);
            if (!monster.game.inside(neighbor, monster.getLevel())) continue;
            Decor decor = monster.game.getGrid(monster.getLevel()).get(neighbor);
            boolean walkable = true;
            if (decor != null)
                walkable = decor.isWalkable(monster);
            List<Entity> entities = monster.game.getGameObjects(neighbor, monster.getLevel());
            for (Entity entity : entities) {
                if (!entity.isWalkable(monster) || entity instanceof Explosion) {
                    walkable = false;
                    break;
                }
            }
            if (walkable) {
                PositionNode positionNode = new PositionNode(neighbor);
                neighborList.add(positionNode);
                positionNode.g = position.g + 1;
                positionNode.parent = position;
            }
        }
        return neighborList;
    }

    // return the value in the set that have the same position (null if it doesn't exists)
    private static PositionNode getSamePosition(PositionNode positionNode, Set<PositionNode> openList) {
        for (PositionNode openPositionNode : openList) {
            if (openPositionNode.position.equals(positionNode.position)) {
                return openPositionNode;
            }
        }
        return null;
    }

    // return the position before the one with no parent
    private static Position getNextPosition (PositionNode positionNode) {
        PositionNode positionNodeParent = positionNode.parent;
        if (positionNodeParent == null) return null;
        while (positionNodeParent.parent != null) {
            positionNode = positionNode.parent;
            positionNodeParent = positionNodeParent.parent;
        }
        return positionNode.position;
    }

    // return the direction to take to go from a to b
    private static Direction findDirection(Position a, Position b) {
        for (Direction direction : Direction.values()) {
            if (direction.nextPosition(a).equals(b)) {
                return direction;
            }
        }
        return null;
    }

}

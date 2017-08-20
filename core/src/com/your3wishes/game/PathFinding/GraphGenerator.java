package com.your3wishes.game.PathFinding;

import com.badlogic.gdx.utils.Array;
import com.your3wishes.game.Utilities.LevelLoader;

/**
 * Created by Your3Wishes on 8/19/2017.
 */

public class GraphGenerator {

    public static GraphImp generateGraph() {
        Array<Node> nodes = new Array<Node>();

        int tileWidth = LevelLoader.lvlTileWidth;
        int tileHeight = LevelLoader.lvlTileHeight;

        // Loop over all tiles in map, starting from
        // bottom left corner iteration left to right, then
        // down to up to create nodes for graph
        for (int y = 0; y < tileHeight; y++) {
            for (int x = 0; x < tileWidth; x++) {
                Node node = new Node();
                nodes.add(node);
            }
        }

        // Generate connections between nodes that don't have an
        // obstacle in them
        for (int y = 0; y < tileHeight; y++) {
            for (int x = 0; x < tileWidth; x++) {
                Boolean current = LevelLoader.obstacleList.get(tileWidth * y + x);
                Boolean up = LevelLoader.obstacleList.get(tileWidth * (y+1) + x);
                Boolean down = LevelLoader.obstacleList.get(tileWidth * (y-1) + x);
                Boolean left = LevelLoader.obstacleList.get(tileWidth * y + (x-1));
                Boolean right = LevelLoader.obstacleList.get(tileWidth * y + (x+1));
                Boolean upLeft = LevelLoader.obstacleList.get(tileWidth * (y+1) + (x-1));
                Boolean upRight = LevelLoader.obstacleList.get(tileWidth * (y+1) + (x+1));
                Boolean downLeft = LevelLoader.obstacleList.get(tileWidth * (y-1) + (x-1));
                Boolean downRight = LevelLoader.obstacleList.get(tileWidth * (y-1) + (x+1));

                // If the current node is not an obstacle, create its connections
                // todo: optimize if statements
                if (current == null || current == false) {
                    Node currentNode = nodes.get(tileWidth * y + x);
                    if (y != 0 && (down == null || down == false)) {
                        Node downNode = nodes.get(tileWidth * (y-1) + x);
                        currentNode.createConnection(downNode, 1);
                    }
                    if (x != 0 && (left == null || left == false)) {
                        Node leftNode = nodes.get(tileWidth * y + (x-1));
                        currentNode.createConnection(leftNode, 1);
                    }
                    if (x != tileWidth - 1 && (right == null || right == false)) {
                        Node rightNode = nodes.get(tileWidth * y + (x+1));
                        currentNode.createConnection(rightNode, 1);
                    }
                    if (y != tileHeight - 1 && (up == null || up == false)) {
                        Node upNode = nodes.get(tileWidth * (y+1) + x);
                        currentNode.createConnection(upNode, 1);
                    }
                    if (x != 0 && y != tileHeight - 1 && (upLeft == null || upLeft == false)) {
                        Node upLeftNode = nodes.get(tileWidth * (y+1) + (x-1));
                        currentNode.createConnection(upLeftNode, 1);
                    }
                    if (x != tileWidth - 1 && y != tileHeight - 1 && (upRight == null || upRight == false)) {
                        Node upRightNode = nodes.get(tileWidth * (y+1) + (x+1));
                        currentNode.createConnection(upRightNode, 1);
                    }
                    if (x != 0 && y != 0 && (downLeft == null || downLeft == false)) {
                        Node downLeftNode = nodes.get(tileWidth * (y-1) + (x-1));
                        currentNode.createConnection(downLeftNode, 1);
                    }
                    if (x != tileWidth - 1 && y != 0 && (downRight == null || downRight == false)) {
                        Node downRightNode = nodes.get(tileWidth * (y-1) + (x+1));
                        currentNode.createConnection(downRightNode, 1);
                    }
                }
            }
        }

        return new GraphImp(nodes);
    }
}

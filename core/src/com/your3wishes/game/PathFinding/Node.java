package com.your3wishes.game.PathFinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.your3wishes.game.Utilities.LevelLoader;

/**
 * Created by Your3Wishes on 8/19/2017.
 */

public class Node {
    private Array<Connection<Node>> connections = new Array<Connection<Node>>();
    public int type;
    public int index;

    public Node() {
        index = Node.Indexer.getIndex();
    }

    public Array<Connection<Node>> getConnections() {
        return connections;
    }

    public void createConnection(Node toNode, float cost) {
        connections.add(new ConnectionImp(this, toNode, cost));
    }

    public int getIndex() {
        return index;
    }

    private static class Indexer {
        private static int index = 0;
         public static int getIndex() {
             return index++;
         }
    }

    public Vector2 getXY() {
        float x = (index % LevelLoader.lvlTileWidth) * LevelLoader.tilePixelWidth + (LevelLoader.tilePixelWidth / 2);
        float y = (index / LevelLoader.lvlTileWidth) * LevelLoader.tilePixelHeight + (LevelLoader.tilePixelHeight / 2);
        //float x = (index % LevelLoader.lvlTileWidth) * LevelLoader.tilePixelWidth ;
        //float y = (index / LevelLoader.lvlTileWidth) * LevelLoader.tilePixelHeight ;
        return new Vector2(x, y);
    }
}

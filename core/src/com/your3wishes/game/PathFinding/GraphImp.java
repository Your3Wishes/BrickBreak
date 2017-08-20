package com.your3wishes.game.PathFinding;


import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;
import com.your3wishes.game.Utilities.LevelLoader;


/**
 * Created by Your3Wishes on 8/19/2017.
 */

public class GraphImp implements IndexedGraph<Node> {
    private Array<Node> nodes = new Array<Node>();

    public GraphImp() { super(); }

    public GraphImp(Array<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public int getIndex(Node node) {
        return node.getIndex();
    }

    @Override
    public int getNodeCount() {
        return nodes.size;
    }

    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        return fromNode.getConnections();
    }

    public Node getNodeByXY(int x, int y) {
        int modX = x / LevelLoader.tilePixelWidth;
        int modY = y / LevelLoader.tilePixelHeight;

        return nodes.get(LevelLoader.lvlTileWidth * modY + modX);
    }
}


package com.your3wishes.game.PathFinding;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.your3wishes.game.Utilities.LevelLoader;

/**
 * Created by Your3Wishes on 8/20/2017.
 */

public class HeuristicImp implements Heuristic<Node> {

    @Override
    public float estimate(Node startNode, Node endNode) {
        int startIndex = startNode.getIndex();
        int endIndex = endNode.getIndex();

        int startY = startIndex / LevelLoader.lvlTileWidth;
        int startX = startIndex % LevelLoader.lvlTileWidth;

        int endY = endIndex / LevelLoader.lvlTileWidth;
        int endX = endIndex % LevelLoader.lvlTileWidth;

        // Distance between two points
        float distance = (float) Math.sqrt(Math.pow(endX, startX) + Math.pow(endY - startY, 2));
        //float distance = Math.abs(startX - endX) + Math.abs(startY - endY);
        return distance;
    }
}

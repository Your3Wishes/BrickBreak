package com.your3wishes.game.PathFinding;


import com.badlogic.gdx.ai.pfa.Connection;

/**
 * Created by Your3Wishes on 8/20/2017.
 */

public class ConnectionImp implements Connection<Node> {
    private Node fromNode;
    private Node toNode;
    private float cost;

    public ConnectionImp(Node fromNode, Node toNode, float cost) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.cost = cost;
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public Node getFromNode() {
        return fromNode;
    }

    @Override
    public Node getToNode() {
        return toNode;
    }
}

package com.your3wishes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.your3wishes.game.PathFinding.GraphPathImp;
import com.your3wishes.game.PathFinding.HeuristicImp;
import com.your3wishes.game.PathFinding.Node;
import com.your3wishes.game.Utilities.Assets;
import com.your3wishes.game.Utilities.LevelLoader;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;

/**
 * Created by Your3Wishes on 8/20/2017.
 */

public class Missile extends Actor implements Pool.Poolable, Freeable {

    protected TextureRegion texture;
    protected Rectangle bounds;
    private IndexedAStarPathFinder<Node> pathFinder;
    private GraphPathImp resultPath;
    public boolean alive = true;
    protected float dy = 1500.0f;
    protected int damage = 25;
    private Node startNode;
    private Node endNode;
    private int currentNodeIndex = 0;
    private float currentDestX;
    private float currentDestY;
    private boolean moving = false;
    private Vector2 velocity = new Vector2();
    private float speed = 350.0f;
    private float acceleration = 150.0f;
    private int newYDir;
    private int newXDir;

    private ShapeRenderer shapeRenderer; // For debugging missle path
    static private boolean projectionMatrixSet = false; // For debugging missle path
    //private Vector2 start = new Vector2();
   // private Vector2 end = new Vector2();

    public Missile() {}

    public Missile(Assets assets, float startX, float startY,
                   float endX, float endY, IndexedAStarPathFinder<Node> pathFinder ) {
        resultPath = new GraphPathImp();
        TextureAtlas atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        texture = new TextureRegion(atlas.findRegion("missile"));
        setPosition(startX, startY);
        setBounds(startX,startY,texture.getRegionWidth(),texture.getRegionHeight());
        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
        shapeRenderer = new ShapeRenderer();

        startNode = LevelLoader.graph.getNodeByXY( (int)startX, (int) startY);
        endNode = LevelLoader.graph.getNodeByXY((int)endX, (int)endY);
        this.pathFinder = pathFinder;
        findPath();

    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(texture, this.getX(), this.getY(),this.getOriginX(), this.getOriginY(),
                this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(),this.getRotation());
        //Draw line of ball launch
        if (MyGame.DEBUG) {
            batch.end();
            if(!projectionMatrixSet){
                shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            }
            for (int i = 0; i < resultPath.getCount() - 1; i++) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.RED);
                //shapeRenderer.line(new Vector2(0,0), new Vector2(100,100));
                shapeRenderer.line(resultPath.get(i).getXY(), resultPath.get(i + 1).getXY());
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.end();
                Gdx.gl.glLineWidth(1);
            }
            batch.begin();

        }
    }

    @Override
    public void act (float delta) {
        speed += acceleration * delta;
        setX(getX() + (velocity.x * delta));
        setY(getY() + (velocity.y * delta));

        // Check if we need to move to next node
        if (newYDir > 0 && getY() > currentDestY ||
                newYDir < 0 && getY() < currentDestY ||
                newXDir > 0 && getX() > currentDestX ||
                newXDir < 0 && getX() < currentDestX) {
            setNextDest();
        }
    }

    public void setVelocity (float toX, float toY) {
        velocity.set(toX - getX(), toY - getY());
        newYDir = Integer.signum((int)(toY - getY()));
        newXDir = Integer.signum((int)(toX - getX()));
        velocity.nor(); // Normalizes the value to be used

        velocity.x *= speed;  // Set speed of the object
        velocity.y *= speed;
    }

    public void setBounds(float x, float y) {
        this.bounds.setX(x);
        this.bounds.setY(y);
    }

    @Override
    public void reset() {
        alive = false;
    }

    public void init() {
        alive = true;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void removeFromStage() {
        this.remove();
    }

    public int getDamage() {
        return damage;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void findPath() {
        pathFinder.searchNodePath(startNode, endNode, new HeuristicImp(), resultPath);
        setNextDest();
    }

    public void setNextDest() {
        if (currentNodeIndex < resultPath.getCount()) {
            currentDestX = resultPath.get(currentNodeIndex).getXY().x;
            currentDestY = resultPath.get(currentNodeIndex).getXY().y;
            setVelocity(currentDestX, currentDestY);
            currentNodeIndex++;
        }
        else {
            velocity.set(0,0);
        }

    }
}

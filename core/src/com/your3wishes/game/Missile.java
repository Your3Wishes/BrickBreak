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
    protected int damage = 75;
    private Node startNode;
    private Node endNode;
    private int currentNodeIndex = 0;
    private float currentDestX;
    private float currentDestY;
    private Vector2 velocity = new Vector2();
    private float speed = 660.0f;
    private float initialSpeed = 660.0f;

    private float acceleration = 655.0f;
    private float theta;
    private float turnRadius = 6.5f;
    private float newAngle;
    private float oldAngle;
    private float angleDirection;
    private boolean arcing = false;
    private int newYDir;
    private int newXDir;

    private ShapeRenderer shapeRenderer; // For debugging missle path
    static private boolean projectionMatrixSet = false; // For debugging missle path

    public Missile(Assets assets, float x, float y) {
        resultPath = new GraphPathImp();
        TextureAtlas atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        texture = new TextureRegion(atlas.findRegion("missile"));
        setPosition(x, y);
        setBounds(x,y,texture.getRegionWidth(),texture.getRegionHeight());
        setOrigin(getWidth() * getScaleX() / 2, getHeight() * getScaleY() /2);
        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
        shapeRenderer = new ShapeRenderer();
    }

    public Missile(Assets assets) {
        resultPath = new GraphPathImp();
        TextureAtlas atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        texture = new TextureRegion(atlas.findRegion("missile"));
        setBounds(0,0,texture.getRegionWidth(),texture.getRegionHeight());
        setOrigin(getWidth() * getScaleX() / 2, getHeight() * getScaleY() /2);
        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
        shapeRenderer = new ShapeRenderer();
    }

//    public Missile(Assets assets, float startX, float startY,
//                   float endX, float endY, IndexedAStarPathFinder<Node> pathFinder ) {
//        resultPath = new GraphPathImp();
//        TextureAtlas atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
//        texture = new TextureRegion(atlas.findRegion("missile"));
//        setPosition(startX, startY);
//        setBounds(startX,startY,texture.getRegionWidth(),texture.getRegionHeight());
//        setOrigin(getWidth() * getScaleX() / 2, getHeight() * getScaleY() /2);
//        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
//        shapeRenderer = new ShapeRenderer();
//
//        startNode = LevelLoader.graph.getNodeByXY( (int)startX, (int) startY);
//        endNode = LevelLoader.graph.getNodeByXY((int)endX, (int)endY);
//        this.pathFinder = pathFinder;
//        //findPath();
//    }

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
           // for (int i = 0; i < resultPath.getCount() - 1; i++) {
//                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//                shapeRenderer.setColor(Color.RED);
//                shapeRenderer.line(resultPath.get(i).getXY(), resultPath.get(i + 1).getXY());
//                shapeRenderer.setColor(Color.RED);
//                shapeRenderer.end();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.rect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
                shapeRenderer.end();
                //Gdx.gl.glLineWidth(1);
           // }
            batch.begin();

        }
    }

    @Override
    public void act (float delta) {
        speed += acceleration * delta;
        setX(getX() + (velocity.x * delta * speed));
        setY(getY() + (velocity.y * delta * speed));

        if (getY() > MyGame.SCREENHEIGHT || getY() < 0) {
            alive = false;
        }

        // Update bounds
        setBounds(getX(), getY());

        // old pathfinding code
//        if (arcing) {
//            // Arc left
//            if (theta > newAngle) {
//                if (theta <= newAngle) arcing = false;
//                else theta -= turnRadius * delta * angleDirection ;
//            }
//            // Arc right
//            else if (theta < newAngle) {
//                if (theta >= newAngle) arcing = false;
//                else theta += turnRadius * delta * angleDirection;
//            }
//            else {
//                arcing = false;
//            }
//            velocity.x = (float)Math.cos(Math.toRadians(theta));
//            velocity.y = (float)Math.sin(Math.toRadians(theta)) ;
//        }
//        setRotation(theta - 90);
//
//        // Check if we need to move to next node
//        if (newYDir > 0 && getY()  > currentDestY ||
//                newYDir < 0 && getY() < currentDestY ||
//                newXDir > 0 && getX()   > currentDestX ||
//                newXDir < 0 && getX() < currentDestX) {
//            setNextDest();
//        }


    }

    public void launch(Vector2 touchPos) {
        velocity.set(touchPos.x - getX(), touchPos.y - getY());
        velocity.nor();
        setRotation(Math.abs((float)Math.toDegrees(Math.atan2(touchPos.y - getY(),touchPos.x - getX()))) - 90);
    }


    public void setBounds(float x, float y) {
        this.bounds.setX(x);
        this.bounds.setY(y);
    }

    @Override
    public void reset() {
        alive = true;
        speed = initialSpeed;
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

//    public void findPath() {
//        if (pathFinder.searchNodePath(startNode, endNode, new HeuristicImp(), resultPath) == true){
//            smoothPath();
//            velocity.set(resultPath.get(currentNodeIndex).getXY().x - getX(), resultPath.get(currentNodeIndex).getXY().y - getY()); // Set first unit vector in path
//            newAngle = (float)Math.toDegrees(Math.atan2(velocity.y,velocity.x));
//            theta = newAngle;
//            setNextDest();
//        }
//
//    }
//
//
//    public void setVelocity (float toX, float toY) {
//        oldAngle = newAngle;
//
//        newAngle = Math.abs((float)Math.toDegrees(Math.atan2(toY - getY(),toX - getX())));
//        angleDirection = Math.abs(newAngle - oldAngle);
//
//        Gdx.app.log("angle Direction", String.valueOf(angleDirection));
//        if (angleDirection == 0)
//            arcing = false;
//        else {
//            arcing = true;
//        }
//
//        velocity.set(toX - getX(), toY - getY());
//        newYDir = Integer.signum((int)(toY - getY()));
//        newXDir = Integer.signum((int)(toX - getX()));
//        velocity.nor(); // Normalizes the value to be used
//    }
//
//    public void setNextDest() {
//        if (currentNodeIndex < resultPath.getCount()) {
//            currentDestX = resultPath.get(currentNodeIndex).getXY().x;
//            currentDestY = resultPath.get(currentNodeIndex).getXY().y;
//            currentNodeIndex++;
//            setVelocity(currentDestX, currentDestY);
//        }
//        else {
//            velocity.set(0,0);
//        }
//
//    }
//
//    public void smoothPath() {
//        GraphPathImp tempPath = new GraphPathImp();
//        for (int i = resultPath.getCount() - 1; i >=2; i--) {
//            Node e = resultPath.get(i-2);
//            Node e2 = resultPath.get(i);
//
//            if (pathFinder.searchNodePath(e, e2, new HeuristicImp(), tempPath) == true) {
//                // Unobstructed path found
//                resultPath.removeIndex(i-1);
//                i--;
//            }
//        }
//    }


}

package com.your3wishes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.your3wishes.game.Utilities.Assets;

/**
 * Created by guita on 7/2/2017.
 */

public class Ball extends Actor implements Pool.Poolable, Freeable {
    private TextureRegion texture;
    private Rectangle bounds;
    private float initialScale = 0.75f;
    private int damage = 25; // Amount of damage dealt to enemy ships
    private float startDx = 90.0f; // Starting/standard x velocity
    private float dx = 50.0f; // Current x velocity
    private float dy = 1050.0f; // Current y velocity
    private float maxDx = 350.0f; // Maximum x velocity
    public boolean brickBounceY = false; // Flag to indicate change in y direction due to bouncing
    public boolean brickBounceX = false; // Flag to indicate change in x directino due to boucning
    public boolean alive;
    public boolean launched = true;
    private Paddle paddle; // Used to position ball on paddle when not launched yet
    public boolean growing = false;
    private float growScale = 1.0f;

    private ShapeRenderer shapeRenderer; // For debugging launch line
    static private boolean projectionMatrixSet = false; // For debugging launch line
    Vector2 start = new Vector2();
    Vector2 end = new Vector2();

    public Ball (Assets assets) {
        TextureAtlas atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        texture = atlas.findRegion("ball");
        this.setScale(initialScale, initialScale);
        setBounds(0,0,texture.getRegionWidth() * getScaleX(),texture.getRegionHeight() * getScaleY());
        bounds = new Rectangle(getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());

        this.setPosition((MyGame.SCREENWIDTH / 2) - (getWidth() / 2), 80);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(texture,this.getX(),getY(),this.getOriginX(),this.getOriginY(),this.getWidth(),
                this.getHeight(),this.getScaleX(), this.getScaleY(),this.getRotation());

        // Draw line of ball launch
        if (MyGame.DEBUG) {
            batch.end();
            if(!projectionMatrixSet){
                shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            }
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.line(start, end);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
            shapeRenderer.end();
            Gdx.gl.glLineWidth(1);
            batch.begin();
        }
    }

    @Override
    public void act (float delta) {
        if (launched) {
            // Move ball using velocities
            this.setX(getX() + dx * delta);
            this.setY(getY() + dy * delta);
        }
        else {
            // Keep Ball attached to paddle
            this.setPosition(paddle.getX() + (paddle.getWidth() * paddle.getScaleX()/2) - (this.getWidth() * this.getScaleX() / 2),
                    paddle.getY() + paddle.getHeight());

        }


        // Check for wall collisions
        if (getX() > MyGame.SCREENWIDTH - getWidth() && dx > 0) {
            dx *= -1;
        }
        else if (getX() < 0 && dx < 0) {
            dx *= -1;
        }
        if (getY() + getHeight() > MyGame.SCREENHEIGHT-90 && dy > 0)
            dy *= -1;
        if (getY() < 0 )
            alive = false;

        // Handle paddleGrow Powerup
        if (growing) {
            // Lerp paddles X Scale to growScale
            setScaleX(MathUtils.lerp(getScaleX(), growScale, 0.8f * delta));
            setScaleY(MathUtils.lerp(getScaleY(), growScale, 0.8f * delta));
        }
        else {
            // Lerp paddles X Scale to original
            setScaleX(MathUtils.lerp(getScaleX(), 0.75f, 0.8f * delta));
            setScaleY(MathUtils.lerp(getScaleY(), 0.75f, 0.8f * delta));
        }
        // Update ball bounds
        setBounds(getX(), getY(), texture.getRegionWidth() * getScaleX(), texture.getRegionHeight() * getScaleY());
        bounds.setWidth(getWidth() * getScaleX());
        bounds.setHeight(getHeight() * getScaleY());

        // Update bounding box
        setBounds(getX(), getY());
    }

    @Override
    public void reset() {
        alive = false;
    }

    public void init() {
        alive = true;
        this.setScale(initialScale, initialScale);
        setBounds(getX(), getY(), texture.getRegionWidth() * getScaleX(), texture.getRegionHeight() * getScaleY());
        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
        growing = false;
    }

    public void launch(float touchX, float touchY) {
        if (launched == false) {
            // Calculate xVelocity to make ball travel to point that was touched
            // Slope = (y2 - y1) / (x2 - x1)
            // x1, y1 = current ball position
            // x2, y2 = touched position

            start.set(getX() + (getWidth() * getScaleX() / 2) , getY() + (getHeight() * getScaleY() / 2));
            end.set(touchX, touchY);

            float slope =(touchY - (getY() + (getHeight() * getScaleY() / 2))) / (touchX - (getX() + (getWidth() * getScaleX() / 2)));
            dx = Math.abs(dy / slope);
            if (touchX < getX()) {
                dx *= -1;
            }

            // Force y velocity to be positive so the ball launched upwards
            dy = Math.abs(dy);
        }
        launched = true;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(float x, float y) {
        this.bounds.setX(x);
        this.bounds.setY(y);
    }

    public float getDy() {
        return dy;
    }

    public float getDx() { return dx; }
    public float getMaxDx() { return maxDx;}

    public float getStartDx () {
        return startDx;
    }
    public void setDy(float vel) {
        dy = vel;
    }

    public void setDx(float vel) { dx = vel; }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getDamage() {
        return damage;
    }

    public void removeFromStage() {
        this.remove();
    }


}

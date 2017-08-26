package com.your3wishes.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.your3wishes.game.Utilities.Assets;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;

/**
 * Created by Your3Wishes on 8/16/2017.
 */

public class EnemyShip extends Actor {

    private TextureRegion texture;
    private Rectangle bounds;
    private int health = 100;
    private float dx = 300;
    private float dy = 0;
    private float startX;
    private float startY;
    private float xDest;
    private float yDest;
    private float wiggleAmount = 25.0f;
    private float wiggleLerpFactor = 2.5f;
    private float yLerpDestUp;
    private float yLerpDestDown;
    private boolean wiggleUp = true;
    public boolean alive = true;
    public boolean fireBullet = false;
    private int bulletDuration = 1000;
    private long bulletStartTime = Long.MAX_VALUE;
    public boolean startEvents = false;
    private float randomNumber;
    private ShapeRenderer shapeRenderer; // For debugging bounding box
    static private boolean projectionMatrixSet = false; // For debugging bounding box
    public enum State {
        IDLE, SWEEPINGDOWN, SWEEPINGUP, STRAFING, WAITINGDOWN, WAITINGUP;
    }
    private State state = State.IDLE;

    public EnemyShip (Assets assets) {
        TextureAtlas atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        texture = atlas.findRegion("enemyShip");
        setBounds(0,0,texture.getRegionWidth() * getScaleX() ,texture.getRegionHeight() * getScaleY());
        bounds = new Rectangle(getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
        shapeRenderer = new ShapeRenderer();
        yLerpDestUp = getY() + wiggleAmount;
        yLerpDestDown = getY() - wiggleAmount;
    }

    public EnemyShip (Assets assets, float x, float y) {
        TextureAtlas atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        texture = atlas.findRegion("enemyShip");
        setBounds(0,0,texture.getRegionWidth() * getScaleX() ,texture.getRegionHeight() * getScaleY());
        setPosition(x, y);
        bounds = new Rectangle(getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
        shapeRenderer = new ShapeRenderer();
        yLerpDestUp = getY() + wiggleAmount;
        yLerpDestDown = getY() - wiggleAmount;
}

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(texture, this.getX(), this.getY(),this.getOriginX(), this.getOriginY(),
                this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(),this.getRotation());


        if (MyGame.DEBUG) {
            batch.end();
            if(!projectionMatrixSet){
                shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            }
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
            shapeRenderer.end();
            batch.begin();
        }
    }

    @Override
    public void act (float delta) {
        super.act(delta);
        if (state == State.IDLE) {
            if (wiggleUp) {
                setY(MathUtils.lerp(getY(), yLerpDestUp, wiggleLerpFactor * delta));
                if (getY() >= yLerpDestUp - 10.0f) {
                    wiggleUp = false;
                }
            }
            else {
                setY(MathUtils.lerp(getY(), yLerpDestDown, wiggleLerpFactor * delta));
                if (getY() <= yLerpDestDown + 10.0f) {
                    wiggleUp = true;
                }
            }
            if (startEvents) {
                randomNumber = MathUtils.random(0.0f, 200.0f);
                if (randomNumber < 1 && randomNumber > 0.5f) state = State.STRAFING;
                if (randomNumber < 0.5f) state = State.SWEEPINGDOWN;
            }

        }
        else if (state == State.STRAFING) {
            setX(getX() + (dx) * delta);

            randomNumber = MathUtils.random(0.0f, 200.0f);
            if (randomNumber < 1) state = State.IDLE;
        }
        else if (state == State.SWEEPINGDOWN) {
            startX = getX();
            startY = getY();
            float xDist = MyGame.SCREENWIDTH/2 - getX(); // x Distance from getX() and center of screen
            xDest = getX() + (xDist / 2);
            yDest = MyGame.SCREENHEIGHT / 3;
            this.addAction(moveTo(xDest, yDest, 3.0f, Interpolation.swing));
            state = State.WAITINGDOWN;
        }
        else if (state == State.SWEEPINGUP) {
            this.addAction(moveTo(startX, startY, 3.0f, Interpolation.swing));
            state = State.WAITINGUP;
        }
        else if (state == State.WAITINGDOWN) {
            if (getX() == xDest && getY() == yDest) {
                state = State.SWEEPINGUP;
            }
        }
        else if (state == State.WAITINGUP) {
            if (getX() == startX && getY() == startY) {
                state = State.IDLE;
            }
        }


        if (getX() + (getWidth() * getScaleX()) > MyGame.SCREENWIDTH || getX() < 0) {
            dx *= -1;
        }

        // Update bounds
        setBounds(getX(), getY());


        tryToShoot();
    }

    private void tryToShoot() {
        if (!fireBullet && System.currentTimeMillis() - bulletDuration > bulletStartTime) {
            fireBullet = true;
           setBulletStartTime();
        }
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void damage(int damage) {
        health -= damage;
        if (health <= 0) {
            alive = false;
        }
    }

    public void setBulletStartTime() {
        bulletStartTime = System.currentTimeMillis();
    }


    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(float x, float y) {
        this.bounds.setX(x);
        this.bounds.setY(y);
    }
}

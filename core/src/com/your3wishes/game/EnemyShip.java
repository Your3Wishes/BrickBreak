package com.your3wishes.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Your3Wishes on 8/16/2017.
 */

public class EnemyShip extends Actor {

    private TextureRegion texture;
    private Rectangle bounds;
    private int health = 100;
    private float dx = 0;
    private float dy = 0;
    private float wiggleAmount = 25.0f;
    private float wiggleLerpFactor = 2.5f;
    private float yLerpDestUp;
    private float yLerpDestDown;
    private boolean wiggleUp = true;
    public boolean alive = true;
    public boolean fireBullet = false;
    private int bulletDuration = 1000;
    private long bulletStartTime;
    private ShapeRenderer shapeRenderer; // For debugging bounding box
    static private boolean projectionMatrixSet = false; // For debugging bounding box
    public enum State {
        IDLE, SWEEPING, STRAFING;
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
        }

        tryToShoot();
    }

    private void tryToShoot() {
        if (!fireBullet && System.currentTimeMillis() - bulletDuration > bulletStartTime) {
            fireBullet = true;
            bulletStartTime = System.currentTimeMillis();
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


    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(float x, float y) {
        this.bounds.setX(x);
        this.bounds.setY(y);
    }
}

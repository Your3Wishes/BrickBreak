package com.your3wishes.game.Bricks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.your3wishes.game.MyGame;
import com.your3wishes.game.ShipBullet;
import com.your3wishes.game.Utilities.Assets;

/**
 * Created by Your3Wishes on 7/2/2017.
 */

public class Brick extends Actor {
    protected TextureRegion texture;
    protected Rectangle bounds;
    protected int bulletHealth = 100; // Health pool for when bullets hit brick
    protected int currBulletHealth = bulletHealth;
    protected int health;
    protected float scaleFactor = 1.0f;
    protected TextureAtlas atlas;
    protected ShapeRenderer shapeRenderer; // For debugging bounding box
    static private boolean projectionMatrixSet = false; // For debugging bounding box


    public boolean alive = true;

//    public Brick (Assets assets) {
//        atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
//        health = 1;
//        setTexture();
//        this.setScale(scaleFactor, scaleFactor);
//        setBounds(0, 0, texture.getRegionWidth() * getScaleX(), texture.getRegionHeight() * getScaleY());
//        bounds = new Rectangle(getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
//    }

//    public Brick (Assets assets, int health) {
//        atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
//        this.health = health;
//        setTexture();
//        this.setScale(scaleFactor, scaleFactor);
//        setBounds(0, 0, texture.getRegionWidth() * getScaleX(), texture.getRegionHeight() * getScaleY());
//        bounds = new Rectangle(getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
//    }

    public Brick () {}

    public Brick (Assets assets, float x, float y, int health) {
        atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        this.health = health;
        setTexture();
        this.setScale(scaleFactor, scaleFactor);
        setPosition(x, y);
        setBounds(x, y, texture.getRegionWidth() * getScaleX(), texture.getRegionHeight() * getScaleY());
        bounds = new Rectangle(getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(texture,this.getX(),getY(),this.getOriginX(),this.getOriginY(),this.getWidth(),
                this.getHeight(),this.getScaleX(), this.getScaleY(),this.getRotation());

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


    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(float x, float y) {
        this.bounds.setX(x);
        this.bounds.setY(y);
    }

    public void ballHit() {
        health--;
        if (health <=0) alive = false;
        setTexture();
    }

    public void bulletHit(int damage) {
        currBulletHealth -= damage;
        if (currBulletHealth <= 0) {
            health--;
            if (health <=0) alive = false;
            setTexture();
            currBulletHealth = bulletHealth;
        }
    }

    public void setHealth(int health) {
        this.health = health;
        if (health <=0) alive = false;
    }

    public void setTexture() {
        switch(health) {
            case 2:
                texture = atlas.findRegion("brick2");
                break;
            case 1:
                texture = atlas.findRegion("brick");
                break;
            default:
                texture = atlas.findRegion("brick");
                break;
        }
    }

}

package com.your3wishes.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Your3Wishes on 7/2/2017.
 */

public class Brick extends Actor {
    private TextureRegion texture;
    private Rectangle bounds;
    private int health;
    private TextureAtlas atlas;


    public boolean alive = true;

    public Brick (Assets assets) {
        atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        health = 1;
        setTexture();
        this.setScale(0.85f, 0.85f);
        setBounds(0, 0, texture.getRegionWidth() * getScaleX(), texture.getRegionHeight() * getScaleY());
        bounds = new Rectangle(getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
    }

    public Brick (Assets assets, int health) {
        atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        this.health = health;
        setTexture();
        this.setScale(0.85f, 0.85f);
        setBounds(0, 0, texture.getRegionWidth() * getScaleX(), texture.getRegionHeight() * getScaleY());
        bounds = new Rectangle(getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(texture,this.getX(),getY(),this.getOriginX(),this.getOriginY(),this.getWidth(),
                this.getHeight(),this.getScaleX(), this.getScaleY(),this.getRotation());
    }


    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(float x, float y) {
        this.bounds.setX(x);
        this.bounds.setY(y);
    }

    public void hit() {
        health--;
        if (health <=0) alive = false;

        setTexture();
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setTexture() {
        switch(health) {
            case 2:
                texture = atlas.findRegion("brick2");
                break;
            case 1:
                texture = atlas.findRegion("brick");
                break;
        }
    }




}

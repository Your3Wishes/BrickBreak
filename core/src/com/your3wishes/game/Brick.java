package com.your3wishes.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Your3Wishes on 7/2/2017.
 */

public class Brick extends Actor {
    private Texture texture;
    private Rectangle bounds;
    private Assets assets;
    private int health;

    public boolean alive = true;

    public Brick (Assets assets) {
        this.assets = assets;
        health = 1;
        setTexture();
        this.setScale(0.8f, 0.8f);
        setBounds(0, 0, texture.getWidth() * getScaleX(), texture.getHeight() * getScaleY());
        bounds = new Rectangle(getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
    }

    public Brick (Assets assets, int health) {
        this.assets = assets;
        this.health = health;
        setTexture();
        this.setScale(0.8f, 0.8f);
        setBounds(0, 0, texture.getWidth() * getScaleX(), texture.getHeight() * getScaleY());
        bounds = new Rectangle(getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(texture,this.getX(),getY(),this.getOriginX(),this.getOriginY(),this.getWidth(),
                this.getHeight(),this.getScaleX(), this.getScaleY(),this.getRotation(),0,0,
                texture.getWidth(),texture.getHeight(),false,false);
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

    public void setTexture() {
        switch(health) {
            case 2: texture = assets.assetManager.get("brick2.png", Texture.class);
                break;
            case 1: texture = assets.assetManager.get("brick.png", Texture.class);
        }
    }




}

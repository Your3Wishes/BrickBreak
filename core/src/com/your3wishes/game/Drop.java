package com.your3wishes.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Your3Wishes on 7/25/2017.
 */

public class Drop extends Actor implements Pool.Poolable {
    protected TextureRegion texture;
    protected TextureAtlas atlas;
    protected Rectangle bounds;
    protected float dx=0.0f;
    protected float dy=0.0f;

    public boolean alive;


    public Drop() {
        dy = MathUtils.random(50.0f,650f);
        dx = MathUtils.random(-80.0f,80.0f);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(texture, this.getX(), getY(), this.getOriginX(), this.getOriginY(), this.getWidth(),
                this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation());

    }

    @Override
    public void act (float delta) {
        dy -= MyGame.GRAVITY * delta;
        // Move item using velocities
        this.setX(getX() + dx * delta);
        this.setY(getY() + dy * delta);

        // Check for wall collisions
        if (getX() > MyGame.SCREENWIDTH - getWidth())
            setX( MyGame.SCREENWIDTH - getWidth());
        if (getX() < 0)
            setX(0);

        // Update bounding box
        setBounds(getX(), getY());
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(float x, float y) {
        this.bounds.setX(x);
        this.bounds.setY(y);
    }

    @Override
    public void reset() {
        dy = MathUtils.random(50.0f,250f);
        dx = MathUtils.random(-30.0f,30.0f);
        alive = true;
    }

}


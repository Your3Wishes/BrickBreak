package com.your3wishes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Joecool321 on 7/9/2017.
 */

public class Coin extends Actor implements Pool.Poolable {
    final Assets assets;
    private Texture texture;
    private Rectangle bounds;
    private float dx=0.0f;
    private float dy=0.0f;

    public boolean alive;



    public Coin(Assets assets) {
        this.assets = assets;
        texture = assets.assetManager.get("coin.png", Texture.class);
        this.setScale(0.8f, 0.8f);
        setBounds(0, 0, texture.getWidth() * getScaleX(), texture.getHeight() * getScaleY());
        bounds = new Rectangle(getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
        dy = MathUtils.random(50.0f,250f);
        dx = MathUtils.random(-30.0f,30.0f);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(texture, this.getX(), getY(), this.getOriginX(), this.getOriginY(), this.getWidth(),
             this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation(), 0, 0,
             texture.getWidth(), texture.getHeight(), false, false);

    }

    @Override
    public void act (float delta) {
        dy -= MyGame.GRAVITY * delta;
        // Move ball using velocities
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

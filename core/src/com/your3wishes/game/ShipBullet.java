package com.your3wishes.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Your3Wishes on 8/14/2017.
 */

public class ShipBullet extends Actor implements Pool.Poolable, Freeable {

    protected TextureRegion texture;
    protected Rectangle bounds;
    public boolean alive = true;
    protected float dy = 1500.0f;
    protected int damage = 25;

    public ShipBullet() {}

    public ShipBullet (com.your3wishes.game.Utilities.Assets assets) {
        TextureAtlas atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        texture = new TextureRegion(atlas.findRegion("shipBullet"));
        setBounds(0,0,texture.getRegionWidth(),texture.getRegionHeight());
        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(texture, this.getX(), this.getY(),this.getOriginX(), this.getOriginY(),
                this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(),this.getRotation());
    }

    @Override
    public void act (float delta) {
        setY(getY() + (dy * delta));

        if (getY() > MyGame.SCREENHEIGHT || getY() < 0) {
            alive = false;
        }

        // Update bounding box
        setBounds(getX(), getY());
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
}

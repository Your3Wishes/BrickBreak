package com.your3wishes.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Your3Wishes on 7/29/2017.
 */

public class Powerup extends Drop {

    public enum Type {
        MULTIBALL, FIREBALL
    }

    private Type type;

    public Powerup(Assets assets) {
        texture = assets.assetManager.get("multiball.png", Texture.class);
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
        super.act(delta);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}

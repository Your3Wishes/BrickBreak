package com.your3wishes.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Your3Wishes on 7/29/2017.
 */

public class Powerup extends Drop {

    private Assets assets;


    public enum Type {
        MULTIBALL, FIREBALL
    }

    private Type type;

    public Powerup(Assets assets) {
        super();
        this.assets = assets;
       // setTexture();
        texture = assets.assetManager.get("multiball.png", Texture.class);
        this.setScale(0.8f, 0.8f);
        setBounds(0, 0, texture.getWidth() * getScaleX(), texture.getHeight() * getScaleY());
        bounds = new Rectangle(getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
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
        setTexture();
    }

    public Type getType() {
        return type;
    }

    public void setTexture() {
        try {
            switch(type) {
                case MULTIBALL:
                    texture = assets.assetManager.get("multiball.png", Texture.class);
                    break;
                case FIREBALL:
                    texture = assets.assetManager.get("fireballPowerup.png", Texture.class);
                    break;
            }
        } catch (Exception e){
            texture = assets.assetManager.get("multiball.png", Texture.class);
        }

    }
}

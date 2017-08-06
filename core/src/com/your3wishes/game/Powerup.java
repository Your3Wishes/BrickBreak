package com.your3wishes.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
        super();
        atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        texture = atlas.findRegion("multiball");
        setTexture();
        this.setScale(0.8f, 0.8f);
        setBounds(0, 0, texture.getRegionWidth() * getScaleX(), texture.getRegionHeight() * getScaleY());
        bounds = new Rectangle(getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(texture, this.getX(), getY(), this.getOriginX(), this.getOriginY(), this.getWidth(),
                this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation());

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
                    texture = atlas.findRegion("multiball");
                    break;
                case FIREBALL:
                    texture = atlas.findRegion("fireballPowerup");
                    break;
            }
        } catch (Exception e){
            texture = atlas.findRegion("multiball");
        }

    }
}

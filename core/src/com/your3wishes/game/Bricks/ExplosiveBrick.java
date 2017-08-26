package com.your3wishes.game.Bricks;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.your3wishes.game.Utilities.Assets;

/**
 * Created by Your3Wishes on 8/9/2017.
 */

public class ExplosiveBrick extends Brick {


    public ExplosiveBrick (Assets assets, float x, float y) {
        atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        health = 1;
        setTexture();
        this.setScale(scaleFactor, scaleFactor);
        setPosition(x, y);
        setBounds(x, y, texture.getRegionWidth() * getScaleX(), texture.getRegionHeight() * getScaleY());
        bounds = new Rectangle(getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
    }

    public void setTexture() {
        switch(health) {
            case 1:
                texture = atlas.findRegion("explosiveBrick");
                break;
        }
    }

}

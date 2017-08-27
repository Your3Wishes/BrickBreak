package com.your3wishes.game.Bricks;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.your3wishes.game.Utilities.Assets;

/**
 * Created by Your3Wishes on 8/27/2017.
 */

public class SolidBrick extends Brick {

    private String textureName;

    public SolidBrick (Assets assets, float x, float y, String textureName) {
        this.textureName = textureName;
        atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        setTexture();
        this.setScale(scaleFactor, scaleFactor);
        setPosition(x, y);
        setBounds(x, y, texture.getRegionWidth() * getScaleX(), texture.getRegionHeight() * getScaleY());
        bounds = new Rectangle(getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
        health = 1;
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void setHealth(int health) {
        // Do nothing
    }

    @Override
    public void ballHit() {
        // Do nothing
    }

    @Override
    public void bulletHit(int damage) {
        // Do nothing
    }

    public void setTexture() {
        if (textureName.equals("solidBrick"))
            texture = atlas.findRegion("solidBrick");
    }
}

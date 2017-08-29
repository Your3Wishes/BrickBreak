package com.your3wishes.game.Drops;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.your3wishes.game.Utilities.Assets;

/**
 * Created by Joecool321 on 7/9/2017.
 */

public class Coin extends Drop {

    public Coin(Assets assets) {
        super();
        atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        texture = atlas.findRegion("coin");
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

    public void hitPaddle() {
        alive = false;
    }


}

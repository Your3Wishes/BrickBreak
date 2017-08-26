package com.your3wishes.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.your3wishes.game.Utilities.Assets;

/**
 * Created by Your3Wishes on 8/14/2017.
 */

public class SideGun extends Actor {

    private TextureRegion texture;
    private Paddle paddle;
    private int sideOfShip;

    // sideOfShip: -1 = left, 1 = right
    public SideGun (Assets assets, Paddle paddle, int sideOfShip) {
        this.paddle = paddle;
        this.sideOfShip = sideOfShip;
        TextureAtlas atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        texture = new TextureRegion(atlas.findRegion("sideGun"));
        setBounds(0,0,texture.getRegionWidth(),texture.getRegionHeight());
        if (sideOfShip == -1) {
            this.setPosition(paddle.getX() - (getWidth() * getScaleX()), paddle.getY());
        }
        else {
            this.setPosition(paddle.getX() + (paddle.getWidth() * paddle.getScaleX()), paddle.getY());
            texture.flip(true, false);
        }

    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
            batch.draw(texture, this.getX(), this.getY(),this.getOriginX(), this.getOriginY(),
                    this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(),this.getRotation());
    }

    @Override
    public void act (float delta) {
        if (sideOfShip == -1) {
            this.setPosition(paddle.getX() - (getWidth() * getScaleX()), paddle.getY());
        }
        else {
            this.setPosition(paddle.getX() + (paddle.getWidth() * paddle.getScaleX()), paddle.getY());
        }
    }

}

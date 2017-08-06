package com.your3wishes.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Created by guita on 7/2/2017.
 */

public class Paddle extends Actor {
    private TextureRegion texture;
    private Rectangle bounds;
    private float dx = 0;
    public float lastX;
    public boolean touched;

    public Paddle (Assets assets) {
        TextureAtlas atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        texture = atlas.findRegion("paddle");
        setBounds(0,0,texture.getRegionWidth(),texture.getRegionHeight());
        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
        this.setPosition((MyGame.SCREENWIDTH / 2) - (getWidth() / 2), 20);

        this.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touched = true;
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                touched = false;
            }
        });
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(texture, this.getX(), this.getY(),this.getOriginX(), this.getOriginY(),
                this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(),this.getRotation());
    }

    @Override
    public void act (float delta) {
        // Set x velocity to the distance between current X and last X multiplied by delta time and constant
        dx = (getX() - lastX) * delta * 3.0f;
        lastX = getX();
    }


    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(float x, float y) {
        this.bounds.setX(x);
        this.bounds.setY(y);
    }

    public void setDx(float vel) {
        dx = vel;
    }

    public float getDx() {
        return dx;
    }
}

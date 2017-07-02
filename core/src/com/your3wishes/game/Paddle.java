package com.your3wishes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Created by guita on 7/2/2017.
 */

public class Paddle extends Actor {
    private Texture texture;
    private Rectangle bounds;
    public boolean touched;

    public Paddle () {
        texture = new Texture(Gdx.files.internal("paddle.png"));
        this.setPosition((MyGame.SCREENWIDTH / 2) - (getWidth() / 2), 20);
        setBounds(0,0,texture.getWidth(),texture.getHeight());
        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());

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
        batch.draw(texture, getX(), getY());
    }


    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(float x, float y) {
        this.bounds.setX(x);
        this.bounds.setY(y);
    }
}

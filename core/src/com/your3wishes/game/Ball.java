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

public class Ball extends Actor {
    private Texture texture;
    private Rectangle bounds;
    private float xVel = 3.0f;
    private float yVel = 3.0f;

    public Ball () {
        texture = new Texture(Gdx.files.internal("ball.png"));
        setBounds(0,0,texture.getWidth(),texture.getHeight());
        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
        this.setPosition((MyGame.SCREENWIDTH / 2) - (getWidth() / 2), 20);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
    }

    @Override
    public void act (float delta) {
        // Move ball using velocities
        this.setX(getX() + xVel);
        this.setY(getY() + yVel);

        // Check for wall collisions
        if (getX() > MyGame.SCREENWIDTH - getWidth() || getX() < 0)
            xVel *= -1;
        if (getY() + getHeight() > MyGame.SCREENHEIGHT)
            yVel *= -1;

        // Update bounding box
        setBounds(getX(), getY());

    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(float x, float y) {
        this.bounds.setX(x);
        this.bounds.setY(y);
    }

    public float getyVel() {
        return yVel;
    }

    public void setyVel(float vel) {
        yVel = vel;
    }
}

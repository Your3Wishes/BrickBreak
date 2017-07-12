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
    final Assets assets;
    private Texture texture;
    private Rectangle bounds;
    private float startDx = 90.0f; // Starting/standard x velocity
    private float dx = 90.0f; // Current x velocity
    private float dy = 330.0f; // Current y velocity
    private float maxDx = 150.0f; // Maximum x velocity
    public boolean brickHit;

    public Ball (Assets assets) {
        this.assets = assets;
        texture = assets.assetManager.get("ball.png", Texture.class);
        this.setScale(0.75f, 0.75f);
        setBounds(0,0,texture.getWidth() * getScaleX(),texture.getHeight() * getScaleY());
        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());

        this.setPosition((MyGame.SCREENWIDTH / 2) - (getWidth() / 2), 80);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(texture,this.getX(),getY(),this.getOriginX(),this.getOriginY(),this.getWidth(),
                this.getHeight(),this.getScaleX(), this.getScaleY(),this.getRotation(),0,0,
                texture.getWidth(),texture.getHeight(),false,false);
    }

    @Override
    public void act (float delta) {
        // Move ball using velocities
        this.setX(getX() + dx * delta);
        this.setY(getY() + dy * delta);

        // Check for wall collisions
        if (getX() > MyGame.SCREENWIDTH - getWidth() && dx > 0) {
            dx *= -1;
        }
        else if (getX() < 0 && dx < 0) {
            dx *= -1;
        }
        if (getY() + getHeight() > MyGame.SCREENHEIGHT-42 && dy > 0)
            dy *= -1;
        if (getY() < 0 )
            setY(MyGame.SCREENHEIGHT-getHeight());

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

    public float getDy() {
        return dy;
    }

    public float getDx() { return dx; }
    public float getMaxDx() { return maxDx;}

    public float getStartDx () {
        return startDx;
    }
    public void setDy(float vel) {
        dy = vel;
    }

    public void setDx(float vel) { dx = vel; }


}

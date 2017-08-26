package com.your3wishes.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.your3wishes.game.Utilities.Assets;

/**
 * Created by guita on 7/2/2017.
 */

public class Paddle extends Actor {
    private TextureRegion texture;
    private Rectangle bounds;
    private float startY = 20;
    private float dx = 0;
    private float midX;
    public boolean touched;
    public boolean growing;
    private float growScale = 1.3f;
    private ShapeRenderer shapeRenderer; // For debugging bounding box
    static private boolean projectionMatrixSet = false; // For debugging bounding box

    public Paddle (Assets assets) {
        TextureAtlas atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        texture = atlas.findRegion("paddle");
        setBounds(0,0,texture.getRegionWidth() * getScaleX() ,texture.getRegionHeight() * getScaleY());
        this.setPosition((MyGame.SCREENWIDTH / 2) - (getWidth() / 2), startY);
        bounds = new Rectangle(getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
        shapeRenderer = new ShapeRenderer();

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


        if (MyGame.DEBUG) {
            batch.end();
            if(!projectionMatrixSet){
                shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            }
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
            shapeRenderer.end();
            batch.begin();
        }
    }

    @Override
    public void act (float delta) {
        midX = getX() + (getWidth()*getScaleX()/2);
        // Handle paddleGrow Powerup
        if (growing) {
            // Lerp paddles X Scale to growScale
            setScaleX(MathUtils.lerp(getScaleX(), growScale, 0.8f * delta));
        }
        else {
            // Lerp paddles X Scale to original
            setScaleX(MathUtils.lerp(getScaleX(), 1.0f, 0.8f * delta));
        }
        // Set new X position so that the midpoint of paddle stays in the same place on screen
        setX(midX - (getWidth()*getScaleX()/2));
        // Update paddle bounds
        setBounds(getX(), getY(), texture.getRegionWidth() * getScaleX(), texture.getRegionHeight() * getScaleY());
        bounds.setWidth(getWidth() * getScaleX());

        // Don't allow paddle to go off the sides of the screen
        if (getX() < 0) {
            setX(0);
        }
        else if (getX() > MyGame.SCREENWIDTH - (getWidth() * getScaleX())) {
            setX(MyGame.SCREENWIDTH - (getWidth() * getScaleX()));
        }
    }

    public void reset() {
        setScaleX(1.0f);
        growing = false;
        // Update paddle bounds
        setBounds(getX(), getY(), texture.getRegionWidth() * getScaleX(), texture.getRegionHeight() * getScaleY());
        bounds.setWidth(getWidth() * getScaleX());
        this.setPosition((MyGame.SCREENWIDTH / 2) - (getWidth() / 2), startY);
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

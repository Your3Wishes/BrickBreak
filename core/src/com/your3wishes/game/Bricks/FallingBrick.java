package com.your3wishes.game.Bricks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.your3wishes.game.Assets;
import com.your3wishes.game.MyGame;

/**
 * Created by Your3Wishes on 8/15/2017.
 */

public class FallingBrick extends Brick {

    private float dy = 0;
    private long startTime;
    private float wiggleDegrees = 4.0f; // The amount the brick will wiggle
    private int spawnToWiggleDuration = 3000; // Duration from spawn to when the brick starts to wiggle
    private int wiggleToFallDuration = 2000; // Druation from wiggle to when the brick starts to falling
    private ShapeRenderer shapeRenderer; // For debugging bounding box
    static private boolean projectionMatrixSet = false; // For debugging bounding box
    private boolean wiggleRight = true;
    private boolean respawn = false;
    private float startX;
    private float startY;
    public enum State {
        WAITING, WIGGLING, FALLING;
    }

    private State state = State.WAITING;

    public FallingBrick(Assets assets, float startX, float startY) {
        super(assets);
        this.startX = startX;
        this.startY = startY;
        setOrigin(getWidth() * getScaleX() / 2, getHeight() * getScaleY() /2);
        startTime = System.currentTimeMillis();
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(texture,this.getX(),getY(),getOriginX(),getOriginY(),this.getWidth(),
                this.getHeight(),this.getScaleX(), this.getScaleY(),this.getRotation());

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
        if (state == State.WAITING) {
            if (System.currentTimeMillis() - spawnToWiggleDuration > startTime) {
                // Move from waiting to wiggling state
                state = State.WIGGLING;
                startTime = System.currentTimeMillis();
            }
        }
        else if (state == State.WIGGLING) {
            if (System.currentTimeMillis() - wiggleToFallDuration > startTime) {
                // Move from wiggling to falling state
                state = State.FALLING;
                respawn = false;
            }
            if (wiggleRight) {
                setRotation(MathUtils.lerp(getRotation(), wiggleDegrees, 15.5f * delta));
                if (getRotation() >= wiggleDegrees - 1.0f) wiggleRight = false;
            }
            else {
                setRotation(MathUtils.lerp(getRotation(), -wiggleDegrees, 15.5f * delta));
                if (getRotation() <= -wiggleDegrees + 1.0f) wiggleRight = true;
            }

            Gdx.app.log("Falling brick rotation: ", String.valueOf(getRotation()));
        }
        else if (state == State.FALLING) {
            // Check if brick has fallen below screen
            if (getY() < 0) {
                setY(MyGame.SCREENHEIGHT);
                respawn = true;
            }
            // Check if brick has landed back in original place
            if (respawn == true && getY() < startY) {
                setY(startY);
                respawn = false;
                state = State.WAITING;
                dy = 0;
                startTime = System.currentTimeMillis();
            }
            else {
                // Lerp rotation back to 0.0f
                setRotation(MathUtils.lerp(getRotation(), 0.0f, 0.8f * delta));
                // Gravity acts on dy
                dy -= MyGame.GRAVITY * delta;

                // Move item using dy velocity
                this.setY(getY() + dy * delta);
            }
        }
        // Update bounding box
        bounds.setPosition(getX(),getY());

    }

    public void setTexture() {
        switch(health) {
            case 1:
                texture = atlas.findRegion("fallingBrick");
                break;
        }
    }
}

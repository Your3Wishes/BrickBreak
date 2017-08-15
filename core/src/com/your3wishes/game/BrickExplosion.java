package com.your3wishes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;


/**
 * Created by Your3Wishes on 8/9/2017.
 */

public class BrickExplosion extends Actor implements Pool.Poolable, Freeable {
    private ParticleEffect effect;
    private Rectangle bounds;
    private ShapeRenderer shapeRenderer; // For debugging bounding box
    static private boolean projectionMatrixSet = false; // For debugging bounding box
    private Vector2 vec;

    public boolean alive;

    public BrickExplosion(Assets assets) {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("brickExplosion.p"), assets.assetManager.get("gameScreen.atlas", TextureAtlas.class));
        bounds = new Rectangle();
        shapeRenderer = new ShapeRenderer();
        vec = new Vector2();
    }

    @Override
    public void act (float delta) {
        effect.update(delta);
        float width = effect.getBoundingBox().getWidth();
        float height = effect.getBoundingBox().getHeight();
        bounds.setPosition(getX() - (width/2),
                           getY() - (height/2));
        bounds.setWidth(width);
        bounds.setHeight(height);

        // If the effect is completed, set alive to false
        // so we can free this explosion from the pool
        if (effect.isComplete()) {
            alive = false;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        effect.draw(batch, Gdx.graphics.getDeltaTime());

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

    public ParticleEffect getEffect() {
        return effect;
    }

    @Override
    public void reset() {
        effect.reset();
        effect.start();
        alive = false;
    }

    public void init() {
        alive = true;
        effect.reset();
        effect.start();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isAlive() {
        return alive;
    }

    public void removeFromStage() {
        this.remove();
    }
}

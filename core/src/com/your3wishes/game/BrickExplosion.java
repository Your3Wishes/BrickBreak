package com.your3wishes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;


/**
 * Created by Your3Wishes on 8/9/2017.
 */

public class BrickExplosion extends Actor implements Pool.Poolable{
    private ParticleEffect effect;
    private Rectangle bounds;

    public boolean alive;

    public BrickExplosion(Assets assets) {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("brickExplosion.p"), assets.assetManager.get("gameScreen.atlas", TextureAtlas.class));
        bounds = new Rectangle();
    }

    @Override
    public void act (float delta) {
        effect.update(delta);
        float width = effect.getBoundingBox().getWidth();
        float height = effect.getBoundingBox().getHeight();
        bounds.setPosition(getX() - (width/2),
                           getY() + (height/2));
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
}

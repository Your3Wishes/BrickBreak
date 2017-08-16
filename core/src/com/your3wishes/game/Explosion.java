package com.your3wishes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by guita on 7/9/2017.
 */

public class Explosion extends Actor implements Pool.Poolable, Freeable{
    private ParticleEffect effect;

    public boolean alive;

    public Explosion(Assets assets) {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("Particles/explosion.p"), assets.assetManager.get("gameScreen.atlas", TextureAtlas.class));
    }

    @Override
    public void act (float delta) {
        effect.update(delta);

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

    public boolean isAlive() {
        return alive;
    }

    public void removeFromStage() {
        this.remove();
    }

    public void init() {
        alive = true;
        effect.reset();
        effect.start();
    }

}

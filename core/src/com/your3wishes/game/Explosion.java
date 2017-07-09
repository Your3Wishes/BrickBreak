package com.your3wishes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by guita on 7/9/2017.
 */

public class Explosion extends Actor {
    ParticleEffect effect;

    public Explosion(ParticleEffect effect) {
        this.effect = effect;
    }

    @Override
    public void act (float delta) {
        effect.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        effect.draw(batch, Gdx.graphics.getDeltaTime()); //define behavior when stage calls Actor.draw()
    }

    public ParticleEffect getEffect() {
        return effect;
    }

}

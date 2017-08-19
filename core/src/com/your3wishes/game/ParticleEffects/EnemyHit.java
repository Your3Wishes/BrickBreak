package com.your3wishes.game.ParticleEffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.your3wishes.game.Utilities.Assets;

/**
 * Created by Your3Wishes on 8/19/2017.
 */

public class EnemyHit extends Explosion {

    public EnemyHit(Assets assets) {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("Particles/enemyHit.p"), assets.assetManager.get("gameScreen.atlas", TextureAtlas.class));
    }
}

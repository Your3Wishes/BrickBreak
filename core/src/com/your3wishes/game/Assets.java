package com.your3wishes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

/**
 * Created by Your3Wishes on 7/11/2017.
 */

public class Assets {

    public AssetManager assetManager = new AssetManager();

    public ParticleEffectLoader.ParticleEffectParameter pep = new ParticleEffectLoader.ParticleEffectParameter();

    public Assets() {
        pep.imagesDir = Gdx.files.internal("");
    }

    public void load() {
        assetManager.load("explosion.p", ParticleEffect.class, pep);
        assetManager.load("fireball.p", ParticleEffect.class, pep);
        assetManager.load("ball.png", Texture.class);
        assetManager.load("brick.png", Texture.class);
        assetManager.load("brick2.png", Texture.class);
        assetManager.load("coin.png", Texture.class);
        assetManager.load("paddle.png", Texture.class);
        assetManager.load("multiball.png", Texture.class);
        assetManager.load("fireballPowerup.png", Texture.class);

    }

    public void dispose() {
        assetManager.dispose();
    }
}

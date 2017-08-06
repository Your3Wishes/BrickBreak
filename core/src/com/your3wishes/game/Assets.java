package com.your3wishes.game;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Your3Wishes on 7/11/2017.
 */

public class Assets {
    public AssetManager assetManager = new AssetManager();

    public Assets() {
    }

    public void load() {
        assetManager.load("gameScreen.atlas", TextureAtlas.class);

    }

    public void dispose() {
        assetManager.dispose();
    }
}

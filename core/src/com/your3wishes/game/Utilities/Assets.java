package com.your3wishes.game.Utilities;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Your3Wishes on 7/11/2017.
 */

public class Assets {
    public AssetManager assetManager = new AssetManager();
    // Sound effects
    public static final String COIN_COLLECT_SOUND = "Sounds/coin_collect.mp3";

    // Music
    public static final String MUSIC_1 = "Music/Mariana.mp3";

    public Assets() {
    }

    public void load() {
        loadImages();
        loadSoundEffects();
        loadMusic();
    }

    public void loadImages() {
        assetManager.load("gameScreen.atlas", TextureAtlas.class);
    }

    public void loadSoundEffects() {
        assetManager.load(COIN_COLLECT_SOUND, Sound.class);
    }

    public void loadMusic() {
        assetManager.load(MUSIC_1, Music.class);
    }

    public void dispose() {
        assetManager.dispose();
    }
}

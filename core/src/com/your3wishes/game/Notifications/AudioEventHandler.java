package com.your3wishes.game.Notifications;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.your3wishes.game.Utilities.Assets;

/**
 * Created by Your3Wishes on 8/29/2017.
 */

public class AudioEventHandler implements Observer {
    Assets assets;
    Music music;
    Sound coinSound;

    public AudioEventHandler(Assets assets) {
        this.assets = assets;
        getSounds();
    }

    @Override
    public void onNotify(Actor actor, GameEvent.Event event) {
        switch (event) {
            case START_GAME:
                music = assets.assetManager.get(Assets.MUSIC_1, Music.class);
                music.setLooping(true);
                music.play();
                break;
            case COIN_COLLECTED:
                coinSound.play(1.0f);
                break;
        }
    }

    private void getSounds() {
        coinSound = assets.assetManager.get(Assets.COIN_COLLECT_SOUND, Sound.class);
    }

}

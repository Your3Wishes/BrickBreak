package com.your3wishes.game.Notifications;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.your3wishes.game.Screens.GameScreen;

/**
 * Created by Your3Wishes on 8/25/2017.
 */

public interface Observer {

    void onNotify(Actor actor, int event);
    <T extends Actor> void onNotify(Array<T> actors, int event);
    void onNotify(GameScreen game, int event);
}

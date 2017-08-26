package com.your3wishes.game.Notifications;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.your3wishes.game.Screens.GameScreen;

/**
 * Created by Your3Wishes on 8/25/2017.
 */

public interface Observer {

    void onNotify(Actor actor, GameEvent.Event event);
    <T extends Actor> void onNotify(Array<T> actors, GameEvent.Event event);
    void onNotify(GameScreen game, GameEvent.Event event);
}

package com.your3wishes.game.Notifications;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.your3wishes.game.EnemyShip;
import com.your3wishes.game.Screens.GameScreen;


/**
 * Created by Your3Wishes on 8/25/2017.
 */

public class GameEventHandler implements Observer{

    public GameEventHandler() {}

    @Override
    public void onNotify(Actor actor, GameEvent.Event event) {

    }

    @Override
    public <T extends Actor> void onNotify(Array<T> actors, GameEvent.Event event) {

    }

    @Override
    public void onNotify(GameScreen game, GameEvent.Event event) {
        switch(event) {
            case EVENTS_START:
                game.startEvents();
                break;
            case NEXT_LEVEL:
                break;
        }
    }
}

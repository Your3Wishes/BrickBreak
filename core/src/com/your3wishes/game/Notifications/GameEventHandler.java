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
    public void onNotify(Actor actor, int event) {

    }

    @Override
    public <T extends Actor> void onNotify(Array<T> actors, int event) {

    }

    @Override
    public void onNotify(GameScreen game, int event) {
        switch(event) {
            case GameEvent.BALL_LAUNCHED:
                for (EnemyShip item : game.getEnemyShips())
                    item.startEvents = true;
                break;
        }
    }
}

package com.your3wishes.game.Notifications;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.your3wishes.game.MyGame;
import com.your3wishes.game.Screens.GameScreen;


/**
 * Created by Your3Wishes on 8/25/2017.
 */

public class GameEventHandler implements Observer{

    private GameScreen game;

    public GameEventHandler(GameScreen game) {
        this.game = game;
    }

    @Override
    public void onNotify(Actor actor, GameEvent.Event event) {
        switch(event) {
            case EVENTS_START:
                game.startEvents();
                break;
            case NEXT_LEVEL:
                break;
            case COIN_COLLECTED:
                game.collectCoin();
                break;
            case MULTIBALL_COLLECTED:
                game.spawnMultiBall();
                break;
            case FIREBALL_COLLECTED:
                game.activateFireball();
                break;
            case SLOWTIME_COLLECTED:
                game.activateSlowTime();
                break;
        }
    }

//    @Override
//    public <T extends Actor> void onNotify(Array<T> actors, GameEvent.Event event) {
//
//    }
//
//    @Override
//    public void onNotify(GameScreen game, GameEvent.Event event) {
//        switch(event) {
//            case EVENTS_START:
//                game.startEvents();
//                break;
//            case NEXT_LEVEL:
//                break;
//            case COIN_COLLECTED:
//                game.collectCoin();
//                break;
//            case MULTIBALL_COLLECTED:
//                game.spawnMultiBall();
//                break;
//            case FIREBALL_COLLECTED:
//                game.activateFireball();
//                break;
//            case SLOWTIME_COLLECTED:
//                game.activateSlowTime();
//                break;
//        }
//    }
}

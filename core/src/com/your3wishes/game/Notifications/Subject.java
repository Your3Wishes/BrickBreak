package com.your3wishes.game.Notifications;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.your3wishes.game.Screens.GameScreen;

/**
 * Created by Your3Wishes on 8/25/2017.
 */

public class Subject {
    private Array<Observer> observers;

    public Subject() {
        observers = new Array<Observer>();
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        for (int i = 0; i < observers.size; i++) {
            if (observers.get(i) == observer) {
                observers.removeIndex(i);
                return;
            }
        }
    }

    protected void notify(Actor actor, int event) {
        for (Observer item : observers) {
            item.onNotify(actor, event);
        }
    }

    protected <T extends Actor> void  notify(Array<T> actors, int event) {
        for (Observer item : observers) {
            item.onNotify(actors, event);
        }
    }

    protected void notify(GameScreen game, int event) {
        for (Observer item : observers) {
            item.onNotify(game, event);
        }
    }

}

package com.your3wishes.game.Notifications;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.your3wishes.game.Screens.GameScreen;

/**
 * Created by Your3Wishes on 8/25/2017.
 */

public class Notifier {
    private Array<Observer> observers;

    public Notifier() {
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

    public void notify(Actor actor, GameEvent.Event event) {
        for (Observer item : observers) {
            item.onNotify(actor, event);
        }
    }

}

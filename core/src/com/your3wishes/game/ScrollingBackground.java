package com.your3wishes.game;

/**
 * Created by Joe on 8/9/2017.
 */


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class ScrollingBackground extends Actor{

    public static final int DEFAULT_SPEED = 50;
    public static final int ACCELERATION = 50;
    public static final int GOAL_REACH_ACCELERATION = 200;

    Texture image;
    float y1, y2;
    int speed;//In pixels / second
    int goalSpeed;
    float imageScale;
    boolean speedFixed;

    public ScrollingBackground (com.your3wishes.game.Utilities.Assets assets) {
        image = new Texture("background2.png");

        y1 = 0;
        y2 = image.getHeight();
        speed = 0;
        goalSpeed = DEFAULT_SPEED;
        imageScale = MyGame.SCREENWIDTH / image.getWidth();
        speedFixed = true;
    }
    @Override
    public void draw (Batch batch, float deltaTime) {

        //Render
        batch.draw(image, 0, y1, MyGame.SCREENWIDTH, image.getHeight() * imageScale);
        batch.draw(image, 0, y2, MyGame.SCREENWIDTH, image.getHeight() * imageScale);
    }

    public void setSpeed (int goalSpeed) {
        this.goalSpeed = goalSpeed;
    }

    public void setSpeedFixed (boolean speedFixed) {
        this.speedFixed = speedFixed;
    }
    @Override
    public void act (float delta) {
        //Speed adjustment to reach goal
        if (speed < goalSpeed) {
            speed += GOAL_REACH_ACCELERATION * delta;
            if (speed > goalSpeed)
                speed = goalSpeed;
        } else if (speed > goalSpeed) {
            speed -= GOAL_REACH_ACCELERATION * delta;
            if (speed < goalSpeed)
                speed = goalSpeed;
        }

        if (!speedFixed)
            speed += ACCELERATION * delta;

        y1 -= speed * delta;
        y2 -= speed * delta;

        //if image reached the bottom of screen and is not visible, put it back on top
        if (y1 + image.getHeight() * imageScale <= 0)
            y1 = y2 + image.getHeight() * imageScale;

        if (y2 + image.getHeight() * imageScale <= 0)
            y2 = y1 + image.getHeight() * imageScale;
        setBounds(getX(), getY(), image.getWidth() * getScaleX(), image.getHeight() * getScaleY());

    }
}

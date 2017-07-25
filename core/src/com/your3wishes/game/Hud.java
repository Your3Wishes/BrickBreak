package com.your3wishes.game;

/**
 * Created by Joe on 7/15/2017.
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;

    //score && time tracking variables
    private Integer worldTimer;
    private Integer coinCount;
    private float timeCount;
    private Integer score;
    private boolean timeUp;


    //Scene2D Widgets
    private Label countdownLabel, timeLabel, linkLabel, coinLabel;
    private static Label scoreLabel, coinCountLabel;

    public Hud(SpriteBatch sb) {
        //define tracking variables
        worldTimer = 99;
        timeCount = 0;
        coinCount = 0;
        score = 0;

        //setup the HUD viewport using a new camera seperate from gamecam
        //define stage using that viewport and games spritebatch
        viewport = new StretchViewport(MyGame.SCREENWIDTH, MyGame.SCREENHEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //define labels using the String, and a Label style consisting of a font and color
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        coinCountLabel = new Label(String.format("%03d", coinCount), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME LEFT", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        linkLabel = new Label("POINTS", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        coinLabel = new Label("COINS", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //define a table used to organize hud's labels
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        //add labels to table, padding the top, and giving them all equal width with expandX
        table.add(linkLabel).expandX().padTop(10);
        table.add(coinLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(coinCountLabel).expandX();
        table.add(countdownLabel).expandX();

        //add table to the stage
        stage.addActor(table);

    }

    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    public void addScore(int value) {
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    public void addCoin(int value) {
        coinCount += value;
        coinCountLabel.setText(String.format("%03d",coinCount));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public boolean isTimeUp() {
        return timeUp;
    }


    public static Label getScoreLabel() {
        return scoreLabel;
    }

    public Integer getScore() {
        return score;
    }
}

package com.your3wishes.game;

/**
 * Created by Joe on 7/15/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;
    private BitmapFont font;

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
        worldTimer = 0;
        timeCount = 0;
        coinCount = 0;
        score = 0;
        Skin skin = new Skin(Gdx.files.internal("labelpic.json"));

        //setup the HUD viewport using a new camera seperate from gamecam
        //define stage using that viewport and games spritebatch
        viewport = new StretchViewport(MyGame.SCREENWIDTH, MyGame.SCREENHEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //define labels using the String, and skin using json
        countdownLabel = new Label("      "+String.format("%03d", worldTimer), skin);
        scoreLabel = new Label("    "+String.format("%06d", score), skin);
        coinCountLabel = new Label("    "+String.format("%03d", coinCount), skin);
        timeLabel = new Label("     TIMER", skin);
        linkLabel = new Label("    POINTS", skin);
        coinLabel = new Label("   COINS", skin);

        //define a table used to organize hud's labels
        Table table = new Table(skin);
        //table.setBackground(background);
        table.top();
        table.setFillParent(true);

        //add labels to table, padding the top, and giving them all equal width with expandX
        table.add(linkLabel).expandX().fillX();
        table.add(coinLabel).expandX().fillX();
        table.add(timeLabel).expandX().fillX();
        table.row();
        table.add(scoreLabel).expandX().fillX();
        table.add(coinCountLabel).expandX().fillX();
        table.add(countdownLabel).expandX().fillX();


        //add table to the stage
        stage.addActor(table);

    }

    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            if (worldTimer <= 999) {
                worldTimer++;
            } else {
                timeUp = true;
            }
            countdownLabel.setText("      "+String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    public void addScore(int value) {
        score += value;
        scoreLabel.setText("    "+String.format("%06d", score));
    }

    public void addCoin(int value) {
        coinCount += value;
        coinCountLabel.setText("    "+String.format("%03d",coinCount));
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

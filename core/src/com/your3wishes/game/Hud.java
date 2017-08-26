package com.your3wishes.game;

/**
 * Created by Joe on 7/15/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;
    private BitmapFont font;
    private com.your3wishes.game.Screens.GameScreen game;

    //score && time tracking variables
    private Integer worldTimer;

    private float timeCount;

    private boolean timeUp;



    //Scene2D Widgets
    private Label countdownLabel, timeLabel, linkLabel, coinLabel, lifeLabel;
    private static Label scoreLabel, coinCountLabel, lifeCountLabel;

    public Hud(SpriteBatch sb, com.your3wishes.game.Screens.GameScreen game) {
        //define tracking variables
        this.game = game;
        worldTimer = 0;
        timeCount = 0;

        Skin skin = new Skin(Gdx.files.internal("labelpic.json"));

        //setup the HUD viewport using a new camera seperate from gamecam
        //define stage using that viewport and games spritebatch
        viewport = new StretchViewport(MyGame.SCREENWIDTH, MyGame.SCREENHEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //define labels using the String, and skin using json
        countdownLabel = new Label("      "+String.format("%03d", worldTimer), skin);
        scoreLabel = new Label("    "+String.format("%06d", game.getScore()), skin);
        coinCountLabel = new Label("    "+String.format("%03d", game.getCoinsCollected()), skin);
        lifeCountLabel = new Label("    "+String.format("%03d",game.getLife())+"%", skin);
        lifeLabel = new Label("     LIFE", skin);
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
        table.add(lifeLabel).expandX().fillX();
        table.row();
        table.add(scoreLabel).expandX().fillX();
        table.add(coinCountLabel).expandX().fillX();
        table.add(countdownLabel).expandX().fillX();
        table.add(lifeCountLabel).expandX().fillX();


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
            lifeCountLabel.setText("    "+String.format("%03d",game.getLife())+"%");
            scoreLabel.setText("    "+String.format("%06d", game.getScore()));
            coinCountLabel.setText("    "+String.format("%03d",game.getCoinsCollected()));
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public boolean isTimeUp() {
        return timeUp;
    }
}

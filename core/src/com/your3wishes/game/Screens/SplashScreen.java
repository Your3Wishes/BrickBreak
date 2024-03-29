package com.your3wishes.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.your3wishes.game.MyGame;

/**
 * Created by guita on 7/2/2017.
 */

public class SplashScreen implements Screen {
    final MyGame game;

    OrthographicCamera camera;

    public SplashScreen(final MyGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.SCREENWIDTH, game.SCREENWIDTH);
    }

    @Override
    public void show() {
        //test

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to Brick Break! ", 250, 700);
        game.font.draw(game.batch, "Tap anywhere to begin!", 260, 600);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

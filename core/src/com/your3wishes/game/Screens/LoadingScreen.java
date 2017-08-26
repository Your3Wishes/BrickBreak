package com.your3wishes.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.your3wishes.game.MyGame;

/**
 * Created by Your3Wishes on 7/11/2017.
 */

public class LoadingScreen implements Screen {
    final MyGame game;

    OrthographicCamera camera;

    public LoadingScreen(final MyGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.SCREENWIDTH, game.SCREENWIDTH);
    }

    @Override
    public void show() {
        game.assets.load();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);


        if (game.assets.assetManager.update()) {
            game.setScreen(new com.your3wishes.game.Screens.SplashScreen(game));
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

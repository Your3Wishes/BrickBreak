package com.your3wishes.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.your3wishes.game.MyGame;

/**
 * Created by Joe on 8/13/2017.
 */

public class GameOverScreen implements Screen{
        final MyGame game;

        OrthographicCamera camera;

        public GameOverScreen(final MyGame game) {
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
            Gdx.gl.glClearColor(240/255f, 128/255f, 128/255f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            camera.update();
            game.batch.setProjectionMatrix(camera.combined);

            game.batch.begin();
            game.font.draw(game.batch, "Game Over ", 500, 700);
            game.font.draw(game.batch, "Tap anywhere to Restart!", 260, 600);
            game.batch.end();

            if (Gdx.input.isTouched()) {
                game.setScreen(new com.your3wishes.game.Screens.GameScreen(game));
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



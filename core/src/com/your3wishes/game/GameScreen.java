package com.your3wishes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.Iterator;

import static java.lang.Math.abs;

/**
 * Created by guita on 7/2/2017.
 */

public class GameScreen implements Screen {
    final MyGame game;

    private Stage stage;
    private Paddle paddle;
    private Ball ball;
    private Array<Brick> bricks;
    private Brick brick;


    public GameScreen(final MyGame game) {
        this.game = game;

        stage = new Stage(new StretchViewport(MyGame.SCREENWIDTH, MyGame.SCREENHEIGHT, game.camera));
        Gdx.input.setInputProcessor(stage);

        // Add paddle to stage
        paddle = new Paddle();
        stage.addActor(paddle);

        // Add ball to stage
        ball = new Ball();
        stage.addActor(ball);

        // Initialize bricks array
        bricks = new Array<Brick>();

        // Spawn bricks
        for (int i = 0; i <= 8; i++) {
            for (int j = 0; j <= 3; j++) {
                brick = new Brick();
                brick.setX(40 + i * brick.getWidth() * brick.getScaleX());
                brick.setY(500 + (j * (brick.getHeight() * brick.getScaleY() + 10)));
                brick.setBounds(brick.getX(), brick.getY());
                bricks.add(brick);
                stage.addActor(brick);
            }

        }

    }

    @Override
    public void show() {
    }

    @Override
    public void render (float delta) {





        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.camera.update();
        update(delta);
        stage.draw();
    }

    private void update(float delta) {
        stage.act(delta);
        checkCollisions();
        handleInput();

    }

    private void checkCollisions() {
        // Paddle and ball collision
        if (paddle.getBounds().overlaps(ball.getBounds()) && ball.getDy() < 0) {
            ball.setDy(ball.getDy() * -1);

            // Add a slight random speed fluctuation of the x velocity
            ball.setDx(ball.getDx() * MathUtils.random(0.4f, 2.5f));
            // Don't allow the ball to go faster than it's maxDx
            if (abs(ball.getDx()) > ball.getMaxDx()) {
                if (ball.getDx() < 0) ball.setDx(-ball.getMaxDx());
                else ball.setDx(ball.getMaxDx());
            }
            //ball.setDx(ball.getDx() * -1);
            // ball.setDx(ball.getStartDx() - paddle.getDx());
        }

        // Check for collisions between ball and bricks
        // Using an iterator for safe removal of items while iterating
        ball.brickHit = false;
        for (Iterator<Brick> iterator = bricks.iterator(); iterator.hasNext();) {
            Brick brick = iterator.next();
            if (brick.getBounds().overlaps(ball.getBounds())) {
                // Remove the current element from the iterator and the list.
                iterator.remove();
                brick.remove();
                ball.brickHit = true;


            }
        }

        if (ball.brickHit) {
            // Bounce ball
            ball.setDy(ball.getDy() * -1);
        }

    }

    private void handleInput() {
        // Control paddle
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(touchPos);
            if (paddle.touched) {
                paddle.setX(touchPos.x - paddle.getWidth() / 2);
                paddle.setBounds(paddle.getX(), paddle.getY());
            }
        }

        // Control ball for debugging
        if (MyGame.DEBUG) {
            if (Gdx.input.isTouched()) {
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                game.camera.unproject(touchPos);
                ball.setX(touchPos.x - ball.getWidth() / 2);
                ball.setY(touchPos.y - ball.getHeight() / 2);
                ball.setBounds(ball.getX(), ball.getY());
            }
        }

    }

    @Override
    public void dispose () {
        stage.dispose();
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

}

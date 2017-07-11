package com.your3wishes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.Iterator;
import java.util.Random;

import static com.badlogic.gdx.graphics.Color.WHITE;
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
    private Explosion explosion;
    private final Array<Explosion> explosions;
    private final Pool<Explosion> explosionPool;
    private Coin coin;
    private final Array<Coin> coins;
    private final Pool<Coin> coinPool;
    private int coinCount;
    private int coinsCaught;
    private int points;
    private float randomNumber;
    private int maxCoinCount;
    private float lastTouchX;
    //added for hud
    private OrthographicCamera hudCam;
    private SpriteBatch hudSpriteBatch;
    private BitmapFont font;
    //private TextureRegion hudLayoutTextureRegion;
    //private TextureRegion progressIndicatorTextureRegion;
    private Texture texture;



    public GameScreen(final MyGame game) {
        this.game = game;

        //added for hud

        texture = new Texture(Gdx.files.internal("images.png"));
        hudCam = new OrthographicCamera(MyGame.SCREENWIDTH, MyGame.SCREENHEIGHT);
        hudSpriteBatch = new SpriteBatch();
        font = new BitmapFont();
        //hudLayoutTextureRegion = atlas.findRegion("hudLayout");

        // Setup stage
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

        // Initialize coins
        maxCoinCount=30;
        coins = new Array<Coin>();
        coinPool = new Pool<Coin>() {
            @Override
            protected  Coin newObject() {
                return new Coin();
            }
        };

        // Initialize explosions
        explosions = new Array<Explosion>();
        explosionPool = new Pool<Explosion>() {
            @Override
            protected  Explosion newObject() {
                return new Explosion();
            }
        };

        // Spawn bricks
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 3; j++) {
                brick = new Brick();
                coin = new Coin();
                brick.setX(16 + i * brick.getWidth() * brick.getScaleX());
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
        
        //added for hud
        // camera focus: x-axis middle of level, y-axis middle of level, z-axis ignored
        hudCam.position.set(MyGame.SCREENWIDTH/2, MyGame.SCREENHEIGHT/2, 0.0f);
        // update camera
        hudCam.update();
        //hudCam.apply(Gdx.gl20);
        // set the projection matrix
        hudSpriteBatch.setProjectionMatrix(hudCam.combined);
        // draw something
        hudSpriteBatch.begin();
        hudSpriteBatch.draw(texture, 0, MyGame.SCREENHEIGHT - 42, MyGame.SCREENWIDTH, 42);
        font.setColor(WHITE);
        font.draw(hudSpriteBatch, "Points: "+points, 0.0f, MyGame.SCREENHEIGHT - 20);
        //TODO add points
        font.draw(hudSpriteBatch, coinsCaught + " coins" , 100.0f, MyGame.SCREENHEIGHT - 20);
        //TODO add coin count
        font.draw(hudSpriteBatch, "Level 1", MyGame.SCREENWIDTH/2 - 16, MyGame.SCREENHEIGHT - 4);
        //TODO added Level
        // TODO Replace placeholder string with real best time once implemented
        font.draw(hudSpriteBatch, "01:23:45", MyGame.SCREENWIDTH - 68, MyGame.SCREENHEIGHT - 16);
        // TODO Replace placeholder x-position with value between 10 and 223 (based on progress percent)

        hudSpriteBatch.end();
    }

    private void update(float delta) {
        stage.act(delta);
        checkCollisions();
        handleInput();
        freeCoins();
        freeExplosions();
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
        }

        // Paddle and coin collision. Coin collected
        for (Coin item : coins) {
            if (paddle.getBounds().overlaps(item.getBounds())) {
                item.alive = false;
                coinsCaught++;
                points=points+5;
            }
        }

        // Check for collisions between ball and bricks
        // Using an iterator for safe removal of items while iterating
        ball.brickHit = false;
        for (Iterator<Brick> iterator = bricks.iterator(); iterator.hasNext();) {
            Brick brick = iterator.next();
            if (brick.getBounds().overlaps(ball.getBounds())) {
                // Create explosion and add it to explosionPool
                explosion = explosionPool.obtain();
                explosion.setPosition(brick.getX(), brick.getY());
                explosion.getEffect().setPosition(brick.getX(), brick.getY());
                explosion.init();
                explosions.add(explosion);
                stage.addActor(explosion);

                randomNumber = MathUtils.random(0.0f, 100.0f);
                if ((randomNumber > 10) & coinCount < maxCoinCount)  {
                    coin = coinPool.obtain();
                    coin.setPosition((brick.getX() + (brick.getWidth() / 4)), brick.getY());
                    coins.add(coin);
                    stage.addActor(coin);
                    coinCount++;
                }
                // Remove the current element from the iterator and the list.
                iterator.remove();
                brick.remove();
                ball.brickHit = true;
                points=points+2;
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
            // If we were touching screen before, move the paddle
            // based on the distance between this touch and last touch
            if (lastTouchX > 0) {
                paddle.setX(paddle.getX() + touchPos.x - lastTouchX);
            }
            // Don't allow paddle to go off the sides of the screen
            if (paddle.getX() < 0) {
                paddle.setX(0);
            }
            else if (paddle.getX() > MyGame.SCREENWIDTH - paddle.getWidth()) {
                paddle.setX(MyGame.SCREENWIDTH - paddle.getWidth());
            }
            // Update paddles bounding box based on new location
            paddle.setBounds(paddle.getX(), paddle.getY());
            // Set the last touch position as this current touch position
            lastTouchX = touchPos.x;
        }
        else {
            lastTouchX = -1; // Indicates we picked up finger
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

    private void freeCoins() {
        for (int i = coins.size; --i >= 0;) {
            coin = coins.get(i);
            if (coin.alive == false) {
                coin.remove(); // Remove coin from stage
                coins.removeIndex(i); // Remove coin from coins array
                coinPool.free(coin); // Remove coin from coinPool
            }
        }
    }

    private void freeExplosions() {
        for (int i = explosions.size; --i >= 0;) {
            explosion = explosions.get(i);
            if (explosion.alive == false) {
                explosion.remove(); // Remove coin from stage
                explosions.removeIndex(i); // Remove coin from coins array
                explosionPool.free(explosion); // Remove coin from coinPool
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

package com.your3wishes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
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
    private final Array<Ball> balls;
    private final Pool<Ball> ballPool;
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
    private Powerup powerup;
    private final Array<Powerup> powerups;
    private final Pool<Powerup> powerupPool;
    private int points;
    private float randomNumber;
    private int maxCoinCount;
    private float lastTouchX;
    // Added for hud
    private Hud hud;
    private OrthographicCamera hudCam;
    private SpriteBatch hudSpriteBatch;
    private BitmapFont font;
    private Texture texture;


    // For logging fps
    FPSLogger fpsLogger = new FPSLogger();



    public GameScreen(final MyGame game) {
        this.game = game;
        //added for hud
        hudSpriteBatch = new SpriteBatch();
        hud = new Hud(hudSpriteBatch);

        // Setup stage
        stage = new Stage(new StretchViewport(MyGame.SCREENWIDTH, MyGame.SCREENHEIGHT, game.camera));
        Gdx.input.setInputProcessor(stage);

        // Add paddle to stage
        paddle = new Paddle(game.assets);
        stage.addActor(paddle);


        // Initialize balls
        balls = new Array<Ball>();
        ballPool = new Pool<Ball>() {
            @Override
            protected  Ball newObject() {
                return new Ball(game.assets);
            }
        };

        // Add ball to stage
        ball = ballPool.obtain();
        balls.add(ball);
        stage.addActor(ball);

        // Initialize bricks array
        bricks = new Array<Brick>();

        // Initialize coins
        maxCoinCount=30;
        coins = new Array<Coin>();
        coinPool = new Pool<Coin>() {
            @Override
            protected  Coin newObject() {
                return new Coin(game.assets);
            }
        };

        // Initialize powerups
        powerups = new Array<Powerup>();
        powerupPool = new Pool<Powerup>() {
            @Override
            protected  Powerup newObject() {
                return new Powerup(game.assets);
            }
        };



        // Initialize explosions
        explosions = new Array<Explosion>();
        explosionPool = new Pool<Explosion>() {
            @Override
            protected  Explosion newObject() {
                return new Explosion(game.assets);
            }
        };

        // Spawn bricks
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 3; j++) {
                if (MathUtils.random(0.0f, 100.0f) < 30.0f)
                    brick = new Brick(game.assets, 2);
                else
                    brick = new Brick(game.assets, 1);
                brick.setX(16 + i * brick.getWidth() * brick.getScaleX());
                brick.setY(1500 + (j * (brick.getHeight() * brick.getScaleY() + 10)));
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
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        hud.update(delta);

    }

    private void update(float delta) {
        stage.act(delta);
        checkCollisions();
        handleInput();
        freeCoins();
        freeExplosions();
        freePowerups();
        fpsLogger.log();
    }

    private void checkCollisions() {
        // Paddle and ball collision
        for (Ball item : balls) {
            if (paddle.getBounds().overlaps(item.getBounds()) && item.getDy() < 0) {
                item.setDy(item.getDy() * -1);

                // Add a slight random speed fluctuation of the x velocity
                item.setDx(item.getDx() * MathUtils.random(0.4f, 2.5f));
                // Don't allow the ball to go faster than it's maxDx
                if (abs(item.getDx()) > item.getMaxDx()) {
                    if (item.getDx() < 0) item.setDx(-item.getMaxDx());
                    else item.setDx(item.getMaxDx());
                }
            }
        }

        // Paddle and coin collision. Coin collected
        for (Coin item : coins) {
            if (paddle.getBounds().overlaps(item.getBounds())) {
                item.alive = false;
                coinsCaught++;
                hud.addCoin(coinsCaught);
                coinsCaught=0;
                points=points+5;
                hud.addScore(points);
                points=0;
            }
        }

        // Paddle and powerup collision.
        for (Powerup item : powerups) {
            if (paddle.getBounds().overlaps(item.getBounds())) {
                item.alive = false;
                switch (item.getType()) {
                    case MULTIBALL:
                        // Spawn ball
                        ball = ballPool.obtain();
                        ball.setPosition((paddle.getX() + (paddle.getWidth() / 2)), paddle.getY() + paddle.getHeight());
                        // Force ball to move upwards
                        ball.setDy(abs(ball.getDy()));
                        balls.add(ball);
                        stage.addActor(ball);
                        break;
                    case FIREBALL:
                        //// TODO: 7/30/2017  Fireball powerup logic goes here

                }
            }
        }

        // Check for collisions between balls and bricks
        // Using an iterator for safe removal of items while iterating
        for (Iterator<Brick> iterator = bricks.iterator(); iterator.hasNext();) {
            Brick brick = iterator.next();
            for (Ball item : balls) {
                if (brick.getBounds().overlaps(item.getBounds())) {
                    // Damage brick
                    brick.hit();
                    if (!brick.alive) {
                        // Remove current element from the iterator
                        iterator.remove();
                        brick.remove();

                        // Create explosion and add it to explosionPool
                        explosion = explosionPool.obtain();
                        explosion.setPosition(brick.getX(), brick.getY());
                        explosion.getEffect().setPosition(brick.getX(), brick.getY());
                        explosion.init();
                        explosions.add(explosion);
                        stage.addActor(explosion);

                        // Spawn coin
                        randomNumber = MathUtils.random(0.0f, 100.0f);
                        if ((randomNumber < 90) & coinCount < maxCoinCount)  {
                            coin = coinPool.obtain();
                            coin.setPosition((brick.getX() + (brick.getWidth() / 4)), brick.getY());
                            coins.add(coin);
                            stage.addActor(coin);
                            coinCount++;
                        }

                        // Spawn powerup
                        if (randomNumber < 50) {
                            powerup = powerupPool.obtain();
                            powerup.setType(Powerup.Type.MULTIBALL);
                            powerup.setPosition((brick.getX() + (brick.getWidth() / 4)), brick.getY());
                            powerups.add(powerup);
                            stage.addActor(powerup);
                        }
                    }
                    item.brickHit = true;
                    points=points+2;
                    hud.addScore(points);
                    points=0;
                    // Move to next brick. No use checking other balls against this brick if it is now destroyed.
                    break;
                }
            }
        }

        // Bounce balls
        for (Ball item: balls) {
            if (item.brickHit) {
                item.setDy(item.getDy() * -1);
                item.brickHit = false;
            }
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

    private void freePowerups() {
        for (int i = powerups.size; --i >= 0;) {
            powerup = powerups.get(i);
            if (powerup.alive == false) {
                powerup.remove(); // Remove powerup from stage
                powerups.removeIndex(i); // Remove powerup from powerups array
                powerupPool.free(powerup); // Remove powerup from powerupPool
            }
        }
    }

    private void freeExplosions() {
        for (int i = explosions.size; --i >= 0;) {
            explosion = explosions.get(i);
            if (explosion.alive == false) {
                explosion.remove(); // Remove explosion from stage
                explosions.removeIndex(i); // Remove explosion from explosions array
                explosionPool.free(explosion); // Remove explosion from explosionPool
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

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
    private ScrollingBackground scrollingbackground;
    private Ball ball;
    private final Array<Ball> balls;
    private final Pool<Ball> ballPool;
    private ShipBullet shipBullet;
    private final Array<ShipBullet> shipBullets;
    private final Pool<ShipBullet> shipBulletPool;
    private Array<Brick> bricks;
    private Brick brick;
    private Explosion explosion;
    private final Array<Explosion> explosions;
    private final Pool<Explosion> explosionPool;
    private FireBall fireball;
    private final Array<FireBall> fireballs;
    private final Pool<FireBall> fireballPool;
    private BrickExplosion brickExplosion;
    private final Array<BrickExplosion> brickExplosions;
    private final Pool<BrickExplosion> brickExplosionPool;
    private SideGun sideGun;
    private final Array<SideGun> sideGuns;
    private int fireBallDuration = 6000;
    private int slowTimeDuration = 5000;
    private int paddleGrowDuration = 5000;
    private int ballGrowDuration = 5000;
    private int shipBulletDuration = 500;
    private long shipBulletStartTime;
    private long fireBallStartTime;
    private long slowTimeStartTime;
    private long paddleGrowStartTime;
    private long ballGrowStartTime;
    private boolean fireballActive = false;
    private boolean slowTimeActive = false;
    private Coin coin;
    private int score;
    private int coinsCollected;
    private final Array<Coin> coins;
    private final Pool<Coin> coinPool;
    private int coinCount;
    private Powerup powerup;
    private final Array<Powerup> powerups;
    private final Pool<Powerup> powerupPool;
    private float randomNumber;
    private int maxCoinCount;
    private float lastTouchX;
    private int life = 100;
    private int ballLifeReduction = 25;
    // Added for hud
    private Hud hud;
    private SpriteBatch hudSpriteBatch;
    private boolean gameOver=false;




    // For logging fps
    FPSLogger fpsLogger = new FPSLogger();



    public GameScreen(final MyGame game) {
        this.game = game;
        //added for hud
        hudSpriteBatch = new SpriteBatch();
        hud = new Hud(hudSpriteBatch, this);

        // Setup stage
        stage = new Stage(new StretchViewport(MyGame.SCREENWIDTH, MyGame.SCREENHEIGHT, game.camera));
        Gdx.input.setInputProcessor(stage);

        //scrolling background
        scrollingbackground = new ScrollingBackground(game.assets);
        stage.addActor(scrollingbackground);

        // Add paddle to stage
        paddle = new Paddle(game.assets);
        stage.addActor(paddle);

        // Add sideGuns to paddle and stage
        sideGuns = new Array<SideGun>();
        sideGuns.add(new SideGun(game.assets, paddle, -1));
        sideGuns.add(new SideGun(game.assets, paddle, 1));
        stage.addActor(sideGuns.get(0));
        stage.addActor(sideGuns.get(1));

        // Initialize ship bullets
        shipBullets = new Array<ShipBullet>();
        shipBulletPool = new Pool<ShipBullet>() {
            @Override
            protected  ShipBullet newObject() {
                return new ShipBullet(game.assets);
            }
        };

        // Initialize balls
        balls = new Array<Ball>();
        ballPool = new Pool<Ball>() {
            @Override
            protected  Ball newObject() {
                return new Ball(game.assets);
            }
        };

        // Add ball to stage and set it to be initially not launched
        spawnInitialBall();

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

        // Initialize brickExplosions
        brickExplosions = new Array<BrickExplosion>();
        brickExplosionPool = new Pool<BrickExplosion>() {
            @Override
            protected  BrickExplosion newObject() {
                return new BrickExplosion(game.assets);
            }
        };

        // Initialize fireballs
        fireballs = new Array<FireBall>();
        fireballPool = new Pool<FireBall>() {
            @Override
            protected  FireBall newObject() {
                return new FireBall(game.assets);
            }
        };

        // Spawn bricks
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 3; j++) {
                randomNumber = MathUtils.random(0.0f, 100.0f);
                if ( randomNumber < 30.0f)
                    brick = new Brick(game.assets, 2);
                else if ( randomNumber < 90.0f)
                    brick = new Brick(game.assets, 1);
                else
                    brick = new ExplosiveBrick(game.assets);
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
        if (slowTimeActive) delta /= 1.5;
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.camera.update();
        update(delta);
        stage.draw();

        //added for hud
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        hud.update(delta);
        if (gameOver == true) {
            game.setScreen(new GameOver(game));
            dispose();
        }

    }

    private void update(float delta) {
        if (MyGame.DEBUG) {
            delta /= 16;
        }
        stage.act(delta);
        handleTimers();
        checkCollisions();
        tryToShoot();
        handleInput();
        freeItems();
        fpsLogger.log();
        if (balls.size <= 0) {
            life -= ballLifeReduction;
            spawnInitialBall();
        }
        if (life <= 0 )
            gameOver=true;
    }

    private void checkCollisions() {
        // Paddle and ball collision
        for (Ball item : balls) {
            if (paddle.getBounds().overlaps(item.getBounds()) && item.getDy() < 0) {
                item.setDy(item.getDy() * -1);

                // Add a slight random speed fluctuation of the x velocity
                item.setDx(item.getDx() * MathUtils.random(0.7f, 2.5f));
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
                coinsCollected++;
                score+=5;
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
                        ball.init();
                        ball.setPosition((paddle.getX() + (paddle.getWidth() / 2)), paddle.getY() + paddle.getHeight());
                        // Force ball to move upwards
                        ball.setDy(abs(ball.getDy()));
                        balls.add(ball);
                        stage.addActor(ball);
                        if (fireballActive) {
                            spawnFireball(ball);
                        }
                        break;
                    case FIREBALL:
                        // Add a fireball particle to all balls and set fireballActive to true
                        if (!fireballActive){
                            for (Ball element : balls) {
                                spawnFireball(element);
                            }
                            fireBallStartTime = System.currentTimeMillis();
                            fireballActive = true;
                        }
                        break;
                    case SLOWTIME:
                        slowTimeActive = true;
                        slowTimeStartTime = System.currentTimeMillis();
                        break;
                    case PADDLEGROW:
                        paddle.growing = true;
                        paddleGrowStartTime = System.currentTimeMillis();
                        break;
                    case BIGBALL:
                        for (Ball tempBall : balls) {
                            tempBall.growing = true;
                            ballGrowStartTime = System.currentTimeMillis();
                        }
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
                    if (fireballActive) brick.setHealth(0);
                    brick.ballHit();
                    if (!brick.alive) {
                        removeBrick(iterator, brick);
                    }
                    if (!fireballActive)
                        item.brickHit = true;
                    score+=2;

                    // Move to next brick. No use checking other balls against this brick if it is now destroyed.
                    break;
                }
            }

        }

        // Check for collisions between BrickExplosions and bricks
        for (Iterator<Brick> iterator = bricks.iterator(); iterator.hasNext();) {
            Brick brick = iterator.next();
            for (ShipBullet item : shipBullets) {
                if (brick.getBounds().overlaps(item.getBounds())) {
                    item.alive = false;
                    // Damage brick
                    brick.bulletHit(item.getDamage());
                    if (!brick.alive) {
                        removeBrick(iterator, brick);
                    }
                    score+=2;
                    // Move to next brick. No use checking other bullets against this brick if it is now destroyed.
                    break;
                }
            }
        }

        // Check for collisions between bullets and bricks
        for (Iterator<Brick> iterator = bricks.iterator(); iterator.hasNext();) {
            Brick brick = iterator.next();
            int brickExplosionsSize = brickExplosions.size;
            for (int i = 0; i < brickExplosionsSize; i++) {
                if (brick.getBounds().overlaps(brickExplosions.get(i).getBounds())) {
                    brick.setHealth(0);
                    brick.ballHit();
                    if (!brick.alive) {
                        removeBrick(iterator, brick);
                    }
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
            else if (paddle.getX() > MyGame.SCREENWIDTH - (paddle.getWidth() * paddle.getScaleX())) {
                paddle.setX(MyGame.SCREENWIDTH - (paddle.getWidth() * paddle.getScaleX()));
            }
            // Update paddles bounding box based on new location
            paddle.setBounds(paddle.getX(), paddle.getY());
            // Set the last touch position as this current touch position
            lastTouchX = touchPos.x;

            // Launch ball if touching top half of screen
            if (touchPos.y >= MyGame.SCREENHEIGHT / 2) {
                balls.get(0).launched = true;
            }
        }
        else {
            lastTouchX = -1; // Indicates we picked up finger
        }

        if (Gdx.input.justTouched()) {
            //tryToShoot();
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

    private void handleTimers() {
        // Check fireball duration
        if (fireballActive && System.currentTimeMillis() - fireBallDuration > fireBallStartTime) {
            for (FireBall item : fireballs) {
                item.alive = false;
            }
            fireballActive = false;
        }
        if (slowTimeActive && System.currentTimeMillis() - slowTimeDuration > slowTimeStartTime) {
            slowTimeActive = false;
        }
        if (paddle.growing && System.currentTimeMillis() - paddleGrowDuration > paddleGrowStartTime) {
            paddle.growing = false;
        }
        if (System.currentTimeMillis() - ballGrowDuration > ballGrowStartTime) {
            for (Ball item : balls) {
                item.growing = false;
            }
        }

    }

    private void removeBrick(Iterator<Brick> iterator, Brick brick) {
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

        // Create brickExplosion and add it to brickExplosionPool
        if (brick instanceof ExplosiveBrick) {
            brickExplosion = brickExplosionPool.obtain();
            brickExplosion.setPosition(brick.getX(), brick.getY());
            brickExplosion.getEffect().setPosition(brick.getX(), brick.getY());
            brickExplosion.init();
            brickExplosions.add(brickExplosion);
            stage.addActor(brickExplosion);
        }

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
            spawnPowerup(brick);
        }
    }


    // Generic method for freeing all items that implement poolable and freeable
    private <T extends Freeable> void freeItems(Array<T> list, Pool<T> pool) {
        T item;
        for (int i = list.size; --i >=0;) {
            item = list.get(i);
            if (item.isAlive() == false) {
                item.removeFromStage();
                list.removeIndex(i);
                pool.free(item);
            }
        }
    }

    private void spawnFireball(Ball ball) {
        fireball = fireballPool.obtain();
        fireball.init();
        fireball.setBall(ball);
        fireball.setPosition(ball.getX() + ball.getWidth()/2,ball.getY() + ball.getWidth()/2);
        fireballs.add(fireball);
        fireball.getEffect().start();
        stage.addActor(fireball);
    }

    private void spawnShipBullets() {
        for (int i = 0; i < sideGuns.size; i++) {
            shipBullet = shipBulletPool.obtain();
            shipBullet.init();
            sideGun = sideGuns.get(i);
            shipBullet.setPosition(sideGun.getX() + (sideGun.getWidth() * sideGun.getScaleX() /2) - (shipBullet.getWidth() * shipBullet.getScaleX()) /2,
                    sideGun.getY() + (sideGun.getHeight() * sideGun.getScaleY()));
            shipBullets.add(shipBullet);
            stage.addActor(shipBullet);
        }
    }

    private void spawnPowerup(Brick brick) {
        powerup = powerupPool.obtain();
        Array<Powerup.Type> powerupTypes = new Array<Powerup.Type>();
        powerupTypes.add(Powerup.Type.MULTIBALL);
        if (!fireballActive) powerupTypes.add(Powerup.Type.FIREBALL);
        if (!slowTimeActive) powerupTypes.add(Powerup.Type.SLOWTIME);
        if (!paddle.growing) powerupTypes.add(Powerup.Type.PADDLEGROW);
        if (System.currentTimeMillis() - ballGrowDuration > ballGrowStartTime) powerupTypes.add(Powerup.Type.BIGBALL);

        randomNumber = MathUtils.random(0.0f, 100.0f);
        float ratio = 100.0f / powerupTypes.size;
        for (int i = 0; i < powerupTypes.size; i++) {
            if (randomNumber >= i * ratio && randomNumber <= (i+1) * ratio){
                powerup.setType(powerupTypes.get(i));
            }
        }
        powerup.setPosition((brick.getX() + (brick.getWidth() * brick.getScaleX() / 2)), brick.getY());
        powerups.add(powerup);
        stage.addActor(powerup);
    }

    private void spawnInitialBall() {
        ball = ballPool.obtain();
        ball.init();
        ball.setPaddle(paddle);
        ball.launched = false;
        balls.add(ball);
        stage.addActor(ball);
    }

    public int getLife() {
        return life;
    }

    public int getScore() {
        return score;
    }

    public int getCoinsCollected(){
        return coinsCollected;
    }

    private void freeItems() {
        freeItems(coins, coinPool);
        freeItems(powerups, powerupPool);
        freeItems(explosions, explosionPool);
        freeItems(balls, ballPool);
        freeItems(brickExplosions, brickExplosionPool);
        freeItems(fireballs, fireballPool);
        freeItems(shipBullets, shipBulletPool);
    }

    private void tryToShoot() {
        if (System.currentTimeMillis() - shipBulletDuration > shipBulletStartTime) {
            spawnShipBullets();
            shipBulletStartTime = System.currentTimeMillis();
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

package com.your3wishes.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.your3wishes.game.Ball;
import com.your3wishes.game.BrickExplosion;
import com.your3wishes.game.Bricks.Brick;
import com.your3wishes.game.Bricks.FallingBrick;
import com.your3wishes.game.Drops.Coin;
import com.your3wishes.game.EnemyBullet;
import com.your3wishes.game.EnemyShip;
import com.your3wishes.game.ParticleEffects.EnemyHit;
import com.your3wishes.game.ParticleEffects.Explosion;
import com.your3wishes.game.Bricks.ExplosiveBrick;
import com.your3wishes.game.ParticleEffects.FireBall;
import com.your3wishes.game.Freeable;
import com.your3wishes.game.Hud;
import com.your3wishes.game.MyGame;
import com.your3wishes.game.Paddle;
import com.your3wishes.game.Drops.Powerup;
import com.your3wishes.game.ScrollingBackground;
import com.your3wishes.game.ShipBullet;
import com.your3wishes.game.SideGun;
import com.your3wishes.game.Utilities.LevelLoader;

import java.util.Iterator;
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
    private EnemyBullet enemyBullet;
    private final Array<EnemyBullet> enemyBullets;
    private final Pool<EnemyBullet> enemyBulletPool;
    private EnemyShip enemyShip;
    private final Array<EnemyShip> enemyShips;
    private Array<Brick> bricks;
    private Brick brick;
    private Explosion explosion;
    private final Array<Explosion> explosions;
    private final Pool<Explosion> explosionPool;
    private EnemyHit enemyHit;
    private final Array<EnemyHit> enemyHits;
    private final Pool<EnemyHit> enemyHitPool;
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
    private int shipBulletDuration = 800;
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
    private float powerupChance = 25.0f;
    private float randomNumber;
    private int maxCoinCount;
    private float lastTouchX;
    private int life = 100;
    private int ballLifeReduction = 25;
    private int fallingBrickLifeReduction = 10;

    // Hud Variables
    private Hud hud;
    private SpriteBatch hudSpriteBatch;
    private boolean gameOver=false;

    // Level loading
    LevelLoader levelLoader;



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

        // Initialize enemy ships
        enemyShips = new Array<EnemyShip>();

        // Initialize enemy ship bullets
        enemyBullets = new Array<EnemyBullet>();
        enemyBulletPool = new Pool<EnemyBullet>() {
            @Override
            protected  EnemyBullet newObject() {
                return new EnemyBullet(game.assets);
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
            protected Coin newObject() {
                return new Coin(game.assets);
            }
        };

        // Initialize powerups
        powerups = new Array<Powerup>();
        powerupPool = new Pool<Powerup>() {
            @Override
            protected Powerup newObject() {
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

        // Initialize enemy hit particle effects
        enemyHits = new Array<EnemyHit>();
        enemyHitPool = new Pool<EnemyHit>() {
            @Override
            protected  EnemyHit newObject() {
                return new EnemyHit(game.assets);
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


        // Load level
        levelLoader = new LevelLoader(this);
        levelLoader.loadLevel("level1");

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
            game.setScreen(new GameOverScreen(game));
            dispose();
        }

    }

    private void update(float delta) {
       // if (MyGame.DEBUG) delta /= 16;
        handleInput();
        stage.act(delta);
        handleTimers();
        checkCollisions();
        tryToShoot();
        checkIfEnemiesShooting();
        freeItems();
        fpsLogger.log();
        if (balls.size <= 0) {
            life -= ballLifeReduction;
            spawnInitialBall();
        }
        if (life <= 0 )
            gameOver=true;
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

        }
        else {
            lastTouchX = -1; // Indicates we picked up finger
        }

        if (Gdx.input.justTouched()) {
            // Note: justTouched() input cooridinates are Y down
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), MyGame.SCREENHEIGHT - Gdx.input.getY(), 0);

            // Launch ball if touching top half of screen
            if (touchPos.y >= MyGame.SCREENHEIGHT / 2) {
                balls.get(0).launch(touchPos.x, touchPos.y);
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
        if (randomNumber < powerupChance) {
            spawnPowerup(brick);
        }
    }

    private void removeEnemyShip(Iterator<EnemyShip> iterator, EnemyShip enemyShip) {
        // Remove current element from the iterator
        iterator.remove();
        enemyShip.remove();

        // Create explosion and add it to explosionPool
        explosion = explosionPool.obtain();
        explosion.setPosition(enemyShip.getX(), enemyShip.getY());
        explosion.getEffect().setPosition(enemyShip.getX(), enemyShip.getY());
        explosion.init();
        explosions.add(explosion);
        stage.addActor(explosion);

        // Spawn coin
        randomNumber = MathUtils.random(0.0f, 100.0f);
        if ((randomNumber < 90) & coinCount < maxCoinCount)  {
            coin = coinPool.obtain();
            coin.setPosition((enemyShip.getX() + (enemyShip.getWidth() / 4)), enemyShip.getY());
            coins.add(coin);
            stage.addActor(coin);
            coinCount++;
        }

        // Spawn powerup
        if (randomNumber < 50) {
            spawnPowerup(enemyShip);
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

    private void spawnPowerup(Actor entity) {
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
        powerup.setPosition((entity.getX() + (entity.getWidth() * entity.getScaleX() / 2)), entity.getY());
        powerups.add(powerup);
        stage.addActor(powerup);
    }

    public void spawnEnemyHitParticle(float x, float y) {
        // Create explosion and add it to explosionPool
        enemyHit = enemyHitPool.obtain();
        enemyHit.setPosition(x, y);
        enemyHit.getEffect().setPosition(x, y);
        enemyHit.init();
        enemyHits.add(enemyHit);
        stage.addActor(enemyHit);
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

    private void freeItems() {
        freeItems(coins, coinPool);
        freeItems(powerups, powerupPool);
        freeItems(explosions, explosionPool);
        freeItems(enemyHits, enemyHitPool);
        freeItems(balls, ballPool);
        freeItems(brickExplosions, brickExplosionPool);
        freeItems(fireballs, fireballPool);
        freeItems(shipBullets, shipBulletPool);
        freeItems(enemyBullets, enemyBulletPool);
    }

    private void checkIfEnemiesShooting() {
        for (EnemyShip item : enemyShips) {
            if (item.fireBullet) {
                item.fireBullet = false;
                enemyBullet = enemyBulletPool.obtain();
                enemyBullet.init();
                enemyBullet.setPosition(item.getX() + (item.getWidth() * item.getScaleX() /2) - (enemyBullet.getWidth() * enemyBullet.getScaleX()) / 2,
                        item.getY() - (enemyBullet.getHeight() * enemyBullet.getScaleY()) );
                enemyBullets.add(enemyBullet);
                stage.addActor(enemyBullet);
            }
        }
    }

    private void tryToShoot() {
        if (System.currentTimeMillis() - shipBulletDuration > shipBulletStartTime) {
            spawnShipBullets();
            shipBulletStartTime = System.currentTimeMillis();
        }

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

        // Check for collisions between balls and bricks. Also falling bricks and paddle
        // Using an iterator for safe removal of items while iterating
        for (Iterator<Brick> iterator = bricks.iterator(); iterator.hasNext();) {
            Brick brick = iterator.next();
            if (paddle.getBounds().overlaps(brick.getBounds())) {
                brick.setHealth(0);
                if (!brick.alive) {
                    removeBrick(iterator, brick);
                    life -= fallingBrickLifeReduction;
                }
                continue;
            }
            for (Ball item : balls) {
                if (brick.getBounds().overlaps(item.getBounds())) {
                    // Damage brick
                    if (fireballActive) brick.setHealth(0);
                    brick.ballHit();
                    if (!brick.alive) {
                        removeBrick(iterator, brick);
                    }
                    if (!fireballActive) {
                        item.brickBounceY = true;
                    }
                    score+=2;

                    // Move to next brick. No use checking other balls against this brick if it is now destroyed.
                    break;
                }
            }

        }

        // Check for collisions between bullets and bricks
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


        // Check for collisions between BrickExplosions and bricks
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

        // Check for collisions between enemy ships and balls
        for (Iterator<EnemyShip> iterator = enemyShips.iterator(); iterator.hasNext();) {
            enemyShip = iterator.next();
            for (Ball ball : balls) {
                if (ball.getBounds().overlaps(enemyShip.getBounds())) {
                    Rectangle enemyRect = enemyShip.getBounds();
                    Rectangle ballRect = ball.getBounds();
                    // Check which side of enemy the ball hit to reflect dx, or dy accordingly
                    if (ballRect.x < enemyRect.x) {
                        ball.setDx(-Math.abs(ball.getDx()));
                    }
                    else if (ballRect.x + ballRect.getWidth() > enemyRect.x + enemyRect.getWidth()) {
                        ball.setDx(Math.abs(ball.getDx()));
                    }
                    if (ballRect.y < enemyRect.y) {
                        ball.setDy(-Math.abs(ball.getDy()));
                    }
                    else if (ballRect.y + ballRect.getHeight() > enemyRect.y + enemyRect.getHeight()) {
                        ball.setDy(Math.abs(ball.getDy()));
                    }

                    enemyShip.damage(ball.getDamage());
                    spawnEnemyHitParticle(ball.getX() + (ball.getWidth() * ball.getScaleX() / 2), ball.getY() + (ball.getHeight() * ball.getScaleY()));
                    if (enemyShip.alive == false) {
                        removeEnemyShip(iterator, enemyShip);
                    }

                    break;
                }
            }
        }

        // Check for collisions between enemy bullets and paddle
        for (EnemyBullet item : enemyBullets) {
            if (paddle.getBounds().overlaps(item.getBounds())) {
                item.alive = false;
                // Damage player
                life -= item.getDamage();
            }
        }

        // Check for collisions between player bullets and enemy
        for (Iterator<EnemyShip> iterator = enemyShips.iterator(); iterator.hasNext();) {
            enemyShip = iterator.next();
            for (ShipBullet item : shipBullets) {
                if (enemyShip.getBounds().overlaps(item.getBounds())) {
                    item.alive = false;
                    // Damage enemy
                    enemyShip.damage(item.getDamage());
                    spawnEnemyHitParticle(item.getX() + (item.getWidth() * item.getScaleX() / 2), item.getY() + (item.getHeight() * item.getScaleY()));
                    if (enemyShip.alive == false) {
                        removeEnemyShip(iterator, enemyShip);
                    }
                }
            }
        }


        // Bounce balls
        for (Ball item: balls) {
            if (item.brickBounceY) {
                item.setDy(item.getDy() * -1);
                item.brickBounceY = false;
            }
            if (item.brickBounceX) {
                item.setDx(item.getDx() * -1);
                item.brickBounceX = false;
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


    public Array<Brick> getBricks() {
        return bricks;
    }

    public Array<EnemyShip> getEnemyShips() {
        return enemyShips;
    }

    public Stage getStage() {
        return stage;
    }

    public MyGame getGame() {
        return game;
    }

}
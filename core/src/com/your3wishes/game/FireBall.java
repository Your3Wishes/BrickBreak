package com.your3wishes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;

import java.util.logging.ConsoleHandler;

/**
 * Created by guita on 8/2/2017.
 */

public class FireBall extends Actor implements Pool.Poolable {
    private ParticleEffect effect;
    private Ball ball; // Pointer to ball that fireball is attached to

    public boolean alive;

    public FireBall(Assets assets) {
        effect = new ParticleEffect((ParticleEffect) assets.assetManager.get("fireball.p"));
        effect.start();
    }

    @Override
    public void act (float delta) {
        effect.setPosition(ball.getX() + (ball.getWidth()*ball.getScaleX()/2),(ball.getY() + ball.getHeight()*ball.getScaleY()/2));
        effect.update(delta);
        Gdx.app.log("fireball", String.valueOf(ball.getX()));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        effect.draw(batch, Gdx.graphics.getDeltaTime());
    }

    public ParticleEffect getEffect() {
        return effect;
    }

    @Override
    public void reset() {
        alive = false;
    }

    public void init() {
        alive = true;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public void setPosition(float x, float y) {
        effect.setPosition(x, y);
    }


}

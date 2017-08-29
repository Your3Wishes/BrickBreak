package com.your3wishes.game.Animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.your3wishes.game.Freeable;
import com.your3wishes.game.Utilities.Assets;

/**
 * Created by Your3Wishes on 8/28/2017.
 */

public class OneTimeAnimation extends Actor implements Pool.Poolable, Freeable {

    private Animation<TextureRegion> animation;
    private TextureRegion currentFrame;
    private boolean alive = true;
    private TextureAtlas atlas;
    private float frameTime;
    private float stateTime;

    public OneTimeAnimation(Assets assets) {
        atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        stateTime = 0f;
    }

    public void setAnimation(String atlasRegion) {
        animation = new Animation<TextureRegion>(0.015f, atlas.findRegions(atlasRegion), Animation.PlayMode.NORMAL);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(stateTime, false);
        batch.draw(currentFrame,getX(),getY());
    }

    @Override
    public void act(float delta) {
        if (animation.isAnimationFinished(stateTime)) {
            alive = false;
        }
    }

    @Override
    public void reset() {
        alive = true;
        stateTime = 0f;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void removeFromStage() {
        this.remove();
    }
}

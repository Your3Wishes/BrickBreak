package com.your3wishes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.your3wishes.game.Drops.Powerup;
import com.your3wishes.game.Utilities.Assets;

/**
 * Created by Your3Wishes on 8/23/2017.
 */

public class DamageExplosion extends Actor implements Pool.Poolable, Freeable {
    protected ParticleEffect effect;
    protected Rectangle bounds;
    private Assets assets;
    private ShapeRenderer shapeRenderer; // For debugging bounding box
    static private boolean projectionMatrixSet = false; // For debugging bounding box
    public boolean alive;

    public enum Type {
        BRICK, MISSILE
    }

    private DamageExplosion.Type type;

    public DamageExplosion() {}

    public DamageExplosion(Assets assets) {
        this.assets = assets;
        effect = new ParticleEffect();
        bounds = new Rectangle();
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void act (float delta) {
        effect.update(delta);
        float width = effect.getBoundingBox().getWidth();
        float height = effect.getBoundingBox().getHeight();
        bounds.setPosition(getX() - (width/2),
                getY() - (height/2));
        bounds.setWidth(width);
        bounds.setHeight(height);

        // If the effect is completed, set alive to false
        // so we can free this explosion from the pool
        if (effect.isComplete()) {
            alive = false;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        effect.draw(batch, Gdx.graphics.getDeltaTime());

        if (MyGame.DEBUG) {
            batch.end();
            if(!projectionMatrixSet){
                shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            }
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
            shapeRenderer.end();
            batch.begin();
        }
    }

    public ParticleEffect getEffect() {
        return effect;
    }

    @Override
    public void reset() {
        effect.reset();
        effect.start();
        alive = false;
    }

    public void init() {
        alive = true;
        effect.reset();
        effect.start();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isAlive() {
        return alive;
    }

    public void removeFromStage() {
        this.remove();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
        setEffect();
    }

    public void setEffect() {
        try {
            switch(type) {
                case BRICK:
                    effect.load(Gdx.files.internal("Particles/brickExplosion.p"), assets.assetManager.get("gameScreen.atlas", TextureAtlas.class));
                    break;
                case MISSILE:
                    effect.load(Gdx.files.internal("Particles/missileExplosion.p"), assets.assetManager.get("gameScreen.atlas", TextureAtlas.class));
                    break;
            }
        } catch (Exception e){
            effect.load(Gdx.files.internal("Particles/brickExplosion.p"), assets.assetManager.get("gameScreen.atlas", TextureAtlas.class));
        }
    }
}


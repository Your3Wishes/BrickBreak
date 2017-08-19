package com.your3wishes.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.your3wishes.game.Utilities.Assets;


/**
 * Created by Your3Wishes on 8/17/2017.
 */

public class EnemyBullet extends ShipBullet {

    public EnemyBullet (Assets assets) {
        TextureAtlas atlas = assets.assetManager.get("gameScreen.atlas", TextureAtlas.class);
        texture = new TextureRegion(atlas.findRegion("enemyBullet"));
        setBounds(0,0,texture.getRegionWidth(),texture.getRegionHeight());
        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
        dy = -1100.0f;
        damage = 5;
    }


}

package com.your3wishes.game.Bricks;


import com.your3wishes.game.Assets;

/**
 * Created by Your3Wishes on 8/9/2017.
 */

public class ExplosiveBrick extends Brick {

    public ExplosiveBrick(Assets assets) {
        super(assets);
    }

    public void setTexture() {
        switch(health) {
            case 1:
                texture = atlas.findRegion("explosiveBrick");
                break;
        }
    }

}

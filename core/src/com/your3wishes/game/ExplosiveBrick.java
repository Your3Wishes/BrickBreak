package com.your3wishes.game;


/**
 * Created by Your3Wishes on 8/9/2017.
 */

public class ExplosiveBrick extends Brick {

    ExplosiveBrick(Assets assets) {
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

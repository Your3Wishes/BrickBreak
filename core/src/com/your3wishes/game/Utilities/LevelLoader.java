package com.your3wishes.game.Utilities;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.your3wishes.game.Bricks.Brick;
import com.your3wishes.game.Bricks.ExplosiveBrick;
import com.your3wishes.game.Bricks.FallingBrick;
import com.your3wishes.game.MyGame;
import com.your3wishes.game.Screens.GameScreen;

/**
 * Created by Your3Wishes on 8/18/2017.
 */

public class LevelLoader {
    final GameScreen gameScreen;
    // Map loading variables
    private TmxMapLoader mapLoader;
    private TiledMap map;

    public LevelLoader(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        mapLoader = new TmxMapLoader();
    }

    public void loadLevel() {
        map = mapLoader.load("Levels/level1.tmx");
        loadBricks();
    }

    public void loadBricks() {
        // Normal bricks
        if (map.getLayers().get("Bricks") != null)
            for (MapObject object : map.getLayers().get("Bricks").getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                Brick temp = new Brick(gameScreen.getGame().assets, rect.getX(), rect.getY(), 1);
                temp.setBounds(temp.getX(), temp.getY());
                gameScreen.getBricks().add(temp);
                gameScreen.getStage().addActor(temp);
            }

        // Falling bricks
        if (map.getLayers().get("FallingBricks") != null)
            for (MapObject object : map.getLayers().get("FallingBricks").getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                FallingBrick temp = new FallingBrick(gameScreen.getGame().assets, rect.getX(), rect.getY());
                gameScreen.getBricks().add(temp);
                gameScreen.getStage().addActor(temp);
            }

        // Explosive bricks
        if (map.getLayers().get("ExplosiveBricks") != null)
            for (MapObject object : map.getLayers().get("ExplosiveBricks").getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                ExplosiveBrick temp = new ExplosiveBrick(gameScreen.getGame().assets, rect.getX(), rect.getY());
                gameScreen.getBricks().add(temp);
                gameScreen.getStage().addActor(temp);
            }
    }
}

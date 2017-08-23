package com.your3wishes.game.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.your3wishes.game.Bricks.Brick;
import com.your3wishes.game.Bricks.ExplosiveBrick;
import com.your3wishes.game.Bricks.FallingBrick;
import com.your3wishes.game.EnemyShip;
import com.your3wishes.game.PathFinding.GraphGenerator;
import com.your3wishes.game.PathFinding.GraphImp;
import com.your3wishes.game.Screens.GameScreen;
import java.util.HashMap;

/**
 * Created by Your3Wishes on 8/18/2017.
 */

public class LevelLoader {
    final GameScreen gameScreen;
    // Map loading variables
    private TmxMapLoader mapLoader;
    private TiledMap map;

    // Static level properties
    public static int lvlTileWidth; // How many tiles wide
    public static int lvlTileHeight; // How many tiles tall
    public static int tilePixelWidth; // Width of tile
    public static int tilePixelHeight; // Height of tile
//    public int lvlTileWidth; // How many tiles wide
//    public int lvlTileHeight; // How many tiles tall
//    public int tilePixelWidth; // Width of tile
//    public int tilePixelHeight; // Height of tile

    // Create a hashmap to store obstacles. Will be referenced by GraphGenerator when
    // creating connections between nodes
    public static HashMap<Integer, Boolean> obstacleList = new HashMap<Integer, Boolean>();
    public static GraphImp graph;

    public LevelLoader(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        mapLoader = new TmxMapLoader();
    }

    public void loadLevel(String levelName) {
        // Todo: pass in dynamic level name
        map = mapLoader.load("Levels/" + levelName + ".tmx");

        // Get level properties
        MapProperties properties = map.getProperties();
        lvlTileWidth = properties.get("width", Integer.class) ;
        lvlTileHeight = properties.get("height", Integer.class);
        tilePixelWidth = properties.get("tilewidth", Integer.class);
        tilePixelHeight = properties.get("tileheight", Integer.class);

        loadBricks();
        loadEnemies();

        graph = GraphGenerator.generateGraph();



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
                // Set obstacle nodes  to true in obstacle hashmap
                addObstacleNodes(rect);
            }

        // Falling bricks
        if (map.getLayers().get("FallingBricks") != null)
            for (MapObject object : map.getLayers().get("FallingBricks").getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                FallingBrick temp = new FallingBrick(gameScreen.getGame().assets, rect.getX(), rect.getY());
                gameScreen.getBricks().add(temp);
                gameScreen.getStage().addActor(temp);
                // Set obstacle nodes  to true in obstacle hashmap
                addObstacleNodes(rect);
            }

        // Explosive bricks
        if (map.getLayers().get("ExplosiveBricks") != null)
            for (MapObject object : map.getLayers().get("ExplosiveBricks").getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                ExplosiveBrick temp = new ExplosiveBrick(gameScreen.getGame().assets, rect.getX(), rect.getY());
                gameScreen.getBricks().add(temp);
                gameScreen.getStage().addActor(temp);
                // Set obstacle nodes  to true in obstacle hashmap
                addObstacleNodes(rect);
            }
    }

    public void loadEnemies() {
        // EnemyShip
        if (map.getLayers().get("EnemyShips") != null)
            for (MapObject object : map.getLayers().get("EnemyShips").getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                EnemyShip temp = new EnemyShip(gameScreen.getGame().assets, rect.getX(), rect.getY());
                gameScreen.getEnemyShips().add(temp);
                gameScreen.getStage().addActor(temp);
                // Set obstacle nodes  to true in obstacle hashmap
                addObstacleNodes(rect);
            }
    }

    public void addObstacleNodes(Rectangle rect) {
        for (int i = -1; i < rect.getWidth() / tilePixelWidth +1; i++) {
            for (int j = -1; j < rect.getHeight() / tilePixelHeight + 1; j++) {
                int tileX = (int) rect.getX() + (i * tilePixelWidth);
                int tileY = (int) rect.getY() + (j * tilePixelHeight);
                int modX = tileX / tilePixelWidth;
                int modY = tileY / tilePixelHeight;
                int index = lvlTileWidth * modY + modX;
                // Mark node in hashmap as an obstacle
                obstacleList.put(index, true);
            }
        }
    }
}

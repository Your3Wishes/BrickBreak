package com.your3wishes.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGame extends Game {
	public static final int SCREENWIDTH = 480;
	public static final int SCREENHEIGHT = 800;
	public static final float GRAVITY = 290.0f;
	public static final boolean DEBUG = false;
    public Assets assets;
	public SpriteBatch batch;
	public BitmapFont font;
	public OrthographicCamera camera;
	
	@Override
	public void create () {
        assets = new Assets();
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new LoadingScreen(this));

		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREENWIDTH, SCREENHEIGHT);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		this.getScreen().dispose();
	}
}

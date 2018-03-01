package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.PlayScreen;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	Texture img;
	//IMAGE SCREEN SIZE
	public static final int V_WIDTH= 700;	//400
	public static final int V_HEIGHT = 408;	//208
	public static final float PPM = 100;

	//MUSICS
	public static AssetManager manager;

	//collision bits from Box2D
	public static final short GROUND_BIT = 1;
	public static final short MARIO_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	//ENEMY
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	//items
	public static final short ITEM_BIT = 256;





	@Override
	public void create () {
		batch = new SpriteBatch();
		//LOAD ALL THE musics and sounds
		manager = new AssetManager();
		manager.load("mario_music.ogg", Music.class);
		manager.load("coin.wav",Sound.class);
		manager.load("bump.wav",Sound.class);
		manager.load("breakblock.wav",Sound.class);
		manager.load("powerup_spawn.wav", Sound.class);
		manager.load("loose.wav",Sound.class);
		manager.load("win.wav",Sound.class);
		manager.load("powerup.wav",Sound.class);
		manager.finishLoading();	//block everything

		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		manager.dispose();
		batch.dispose();
		//img.dispose();
	}
}

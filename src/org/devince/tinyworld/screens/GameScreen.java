package org.devince.tinyworld.screens;

import org.devince.tinyworld.Assets;
import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameScreen implements Screen {	
	private static final int ICON_SCALE = 4;
	private static final int PADDING = 10;
	private static final int PADDING_HUD = 20;
	private static final int ICON_SIZE = 4 * ICON_SCALE;
	private BitmapFont font;
	private BitmapFont fontGO;
	private SpriteBatch batch;
	private Sound sndSelect;
	private OnScreenController controller;
	private Sprite coin;
	private Sprite heart;
	private Vector2 scorePos;
	private Vector2 lifePos;
	
	public GameScreen() {				
		this.font = Assets.getNewFont();
		this.font.setColor(Color.GREEN);
		this.fontGO = Assets.getNewFont();
		this.fontGO.setColor(Color.GREEN);
		this.fontGO.setScale(2f);
		this.batch = new SpriteBatch();
		this.sndSelect = Assets.getSound("data/select.wav");
		this.controller = new OnScreenController();
		this.coin = Assets.getSprite("data/score.png");
		this.heart = Assets.getSprite("data/life.png");
		this.coin.setScale(ICON_SCALE);
		float x = Gdx.graphics.getWidth() /2f;
		float y = Gdx.graphics.getHeight() - ICON_SIZE / 2f - PADDING;
		this.coin.setPosition(x, y);
		float xLbl = x + ICON_SIZE / 2f + PADDING;
		float yLbl = Gdx.graphics.getHeight();
		this.scorePos = new Vector2(xLbl, yLbl);
		this.heart.setScale(ICON_SCALE);
		y = Gdx.graphics.getHeight() - ICON_SIZE * 1.5f - PADDING - PADDING_HUD;
		yLbl =Gdx.graphics.getHeight() - PADDING_HUD - ICON_SIZE;
		this.heart.setPosition(x, y);
		this.lifePos = new Vector2(xLbl, yLbl);
		
	}
	
	@Override
	public void dispose() {
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	
	private float cumDelta;

	@Override
	public void render(float delta) {
		this.batch.begin();
		if (TinyWorld.get().isGameOver()) {
			cumDelta += delta;
			if (cumDelta > 1f) {
				fontGO.draw(this.batch, "PRESS ANY KEY", 50, Gdx.graphics.getHeight() / 2f - 75f);
				if (Gdx.input.isKeyPressed(Keys.ANY_KEY) || Gdx.input.justTouched()) {
					this.cumDelta = 0;
					this.sndSelect.play();
					TinyWorld.get().restart();
				}
			}
			fontGO.draw(this.batch, "GAME OVER", 50, Gdx.graphics.getHeight() / 2f + 75f);
			fontGO.draw(this.batch, "SCORE: " + String.valueOf(TinyWorld.get().getScore()), 50, Gdx.graphics.getHeight() / 2f);
		} else {
			this.coin.draw(this.batch);
			this.heart.draw(this.batch);
			// font.draw(this.batch, "Level: " + String.valueOf(TinyWorld.get().getLevel()), Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() - PADDING);
			font.draw(this.batch, String.valueOf(TinyWorld.get().getScore()), scorePos.x, scorePos.y);
			font.draw(this.batch, String.valueOf(TinyWorld.get().getPlayer().getLife()), lifePos.x, lifePos.y);
			this.controller.render(this.batch);
		}
		
		this.batch.end();
	}
	
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		
	}
}

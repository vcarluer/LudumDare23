package org.devince.tinyworld.screens;

import org.devince.tinyworld.Assets;
import org.devince.tinyworld.TinyWorld;
import org.devince.tinyworld.items.GameItem;
import org.devince.tinyworld.items.Invincibility;
import org.devince.tinyworld.items.Life;
import org.devince.tinyworld.items.Score;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;

public class GameScreen implements Screen {	
	private static final int ICON_SCALE = 6;
	private static final int PADDING = 25;
	private static final int PADDING_HUD = 20;
	private static final int ICON_SIZE = 4 * ICON_SCALE;
	private BitmapFont font;
	private BitmapFont fontGO;
	private SpriteBatch batch;
	private Sound sndSelect;
	private OnScreenController controller;
	private Sprite coin;	
	private Sprite heart;
	private Sprite invincible;
	private Vector2 scorePos;
	private Vector2 lifePos;
	private Vector2 scoreSpritePos;
	private Vector2 lifeSpritePos;
	private Texture transBlack;
	
	private Stage stage;
	
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
		this.invincible = Assets.getSprite("data/invincible.png");
		this.coin.setScale(ICON_SCALE);
		float x = Gdx.graphics.getWidth() /2f;
		// float y = Gdx.graphics.getHeight() - getIconSize() / 2f - getPadding;
		float y = getIconSize() * 1.5f + getPadding() + getPaddingHud();
		this.scoreSpritePos = new Vector2(x, y);
		this.coin.setPosition(x, y);
		float xLbl = x + getIconSize() / 2f + getPadding();
		// float yLbl = Gdx.graphics.getHeight();
		float yLbl = getPaddingHud() + getPadding() + getIconSize() * 2f;
		this.scorePos = new Vector2(xLbl, yLbl);
		this.heart.setScale(ICON_SCALE);
		this.invincible.setScale(ICON_SCALE);
		// y = Gdx.graphics.getHeight() - getIconSize() * 1.5f - getPadding() - getPaddingHud();
		y = getIconSize() / 2f + getPadding();
		// yLbl =Gdx.graphics.getHeight() - getPaddingHud() - getIconSize();
		yLbl = getPadding() + getIconSize() * 1f;
		this.lifeSpritePos = new Vector2(x, y);
		this.heart.setPosition(x, y);
		this.invincible.setPosition(x, y);
		this.lifePos = new Vector2(xLbl, yLbl);
		
		this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, this.batch);
		
		this.transBlack = Assets.getTexture("data/blacktranslayer.png");
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
			this.batch.draw(this.transBlack, 50, 50, Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 100);
			cumDelta += delta;
			if (cumDelta > 1f) {
				font.draw(this.batch, "TOUCH SCREEN", 50, Gdx.graphics.getHeight() / 2f - 150f);
				if (Gdx.input.isKeyPressed(Keys.ANY_KEY) || Gdx.input.justTouched()) {
					this.cumDelta = 0;
					this.sndSelect.play();
					TinyWorld.get().restart();
				}
			}						
			fontGO.draw(this.batch, "GAME OVER", 50, Gdx.graphics.getHeight() / 2f + 150f);
			font.draw(this.batch, TinyWorld.get().getKillString(), 50, Gdx.graphics.getHeight() / 2f + 75f);
			fontGO.draw(this.batch, "SCORE: " + String.valueOf(TinyWorld.get().getScore()), 50, Gdx.graphics.getHeight() / 2f);
		} else {
			this.coin.draw(this.batch);
			if (TinyWorld.get().getPlayer().isInvincible()) {
				this.invincible.draw(this.batch);
			} else {
				this.heart.draw(this.batch);
			}
			
			// font.draw(this.batch, "Level: " + String.valueOf(TinyWorld.get().getLevel()), Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() - getPadding());
			font.draw(this.batch, String.valueOf(TinyWorld.get().getScore()), scorePos.x, scorePos.y);
			font.draw(this.batch, String.valueOf(TinyWorld.get().getPlayer().getLife()), lifePos.x, lifePos.y);
			
			this.controller.render(this.batch);
		}
		
		this.batch.end();
		
		if (!TinyWorld.get().isGameOver()) {
			this.stage.act(delta);
			this.stage.draw();
		}
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
	
	public void takeCoin() {
		Score score = new Score();
		// this.actIcon(score, this.scorePos);
		this.actIcon(score, this.scoreSpritePos);
	}
	
	public void takeLife() {
		Life life = new Life();
		this.actIcon(life, this.lifeSpritePos);
	}
	
	public void takeInvincible() {
		Invincibility inv = new Invincibility();
		this.actIcon(inv, this.lifeSpritePos);
	}
	
	private void actIcon(GameItem item, Vector2 targetPos) {
		item.clearActions();
		item.x = Gdx.graphics.getWidth() / 2f;
		item.y = Gdx.graphics.getHeight() / 2f;
		item.scaleX = ICON_SCALE;
		item.scaleY = ICON_SCALE;
		this.stage.addActor(item);
		MoveTo moveTo = MoveTo.$(targetPos.x, targetPos.y, 0.3f);
		moveTo.setCompletionListener(new OnActionCompleted() {
			
			@Override
			public void completed(Action action) {
				stage.removeActor(action.getTarget());
			}
		});
		item.action(moveTo);
	}
	
	public void reset() {
		this.stage.clear();
	}
	
	public OnScreenController getController() {
		return this.controller;
	}
	
	private static float getPadding() {
		return PADDING * OnScreenController.getDensity();
	}
	
	private static float getPaddingHud() {
		return PADDING_HUD * OnScreenController.getDensity();
	}
	
	private static float getIconSize() {
		return ICON_SIZE * OnScreenController.getDensity();
	}
}

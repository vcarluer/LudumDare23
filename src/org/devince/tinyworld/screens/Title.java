package org.devince.tinyworld.screens;

import org.devince.tinyworld.Assets;
import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Title implements Screen {
	private SpriteBatch batch;
	private Sprite sprite;
	private Sound sndSelect;
	private Sprite ga;
	private static float TIME_GA = 5f;
	private float timeElapsed;
	private boolean keyPressed;
	
	public Title() {
		this.batch = new SpriteBatch();
		this.sprite = new Sprite(new TextureRegion(Assets.getTexture("data/title.png"), 0, 0, 800, 600));
		this.sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.sndSelect = Assets.getSound("data/select.wav");
		this.ga =new Sprite(new TextureRegion(Assets.getTexture(Assets.DATA_LOGO), 0, 0, 667, 640));
		this.ga.setScale(0.9375f);
		this.ga.setSize(667 * 0.85f, 640 * 0.85f);
		this.ga.setPosition(Gdx.graphics.getWidth() / 2f - this.ga.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - this.ga.getHeight() / 2f);
		this.keyPressed = false;
	}
	@Override
	public void render(float delta) {
		this.timeElapsed += delta;
		this.batch.begin();
		if (this.timeElapsed < TIME_GA) {
			Gdx.gl.glClearColor(1, 1, 1, 1);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			this.ga.draw(this.batch);
		} else {
			this.sprite.draw(this.batch);
		}
		
		this.batch.end();
		
		
		if (!Gdx.input.isKeyPressed(Keys.VOLUME_DOWN) && !Gdx.input.isKeyPressed(Keys.VOLUME_UP) && (Gdx.input.isKeyPressed(Keys.ANY_KEY) || Gdx.input.justTouched())) {
			if (!this.keyPressed) {
				this.keyPressed = true;
				if (this.timeElapsed < TIME_GA) {
					this.timeElapsed = TIME_GA;
				} else {
					this.sndSelect.play();
					TinyWorld.get().start();
					TinyWorld.get().showHelp();
				}
			}
		} else {
			this.keyPressed = false;
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}

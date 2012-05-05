package org.devince.tinyworld.screens;

import org.devince.tinyworld.Assets;
import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Help implements Screen {
	private SpriteBatch batch;
	private Sprite sprite;
	private Sound sndSelect;
	private boolean keyPressed;
	
	public Help() {
		this.batch = new SpriteBatch();
		this.sprite = new Sprite(new TextureRegion(Assets.getTexture("data/help.png"), 0, 0, 700, 500));
		this.sprite.setSize(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 100);
		this.sprite.setPosition(50, 50);
		this.sndSelect = Assets.getSound("data/select.wav");
		
		if (Gdx.input.isKeyPressed(Keys.ANY_KEY) || Gdx.input.justTouched()) {
			this.keyPressed = true;
		} else {
			this.keyPressed = false;
		}
	}
	@Override
	public void render(float delta) {
		this.batch.begin();
		this.sprite.draw(this.batch);
		this.batch.end();
		if (Gdx.input.isKeyPressed(Keys.ANY_KEY) || Gdx.input.justTouched()) {
			if (!this.keyPressed) {
				this.sndSelect.play();
				TinyWorld.get().start();
				this.keyPressed = true;
			}
		} else {
			this.keyPressed = false;
		}
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		TinyWorld.get().setPause(true);
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

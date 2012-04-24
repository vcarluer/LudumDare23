package org.devince.tinyworld.screens;

import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Title implements Screen {
	private SpriteBatch batch;
	private Sprite sprite;
	private Sound sndSelect;
	
	public Title() {
		this.batch = new SpriteBatch();
		this.sprite = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("data/title.png")), 0, 0, 800, 600));
		this.sprite.setSize(TinyWorld.WIDTH, TinyWorld.HEIGHT);
		this.sndSelect = Gdx.audio.newSound(Gdx.files.internal("data/select.wav"));
	}
	@Override
	public void render(float delta) {
		this.batch.begin();
		this.sprite.draw(this.batch);
		this.batch.end();
		if (Gdx.input.isKeyPressed(Keys.ANY_KEY)) {
			this.sndSelect.play();
			TinyWorld.get().showHelp();
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

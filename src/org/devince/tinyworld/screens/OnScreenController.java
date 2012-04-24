
package org.devince.tinyworld.screens;

import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class OnScreenController {
	TextureRegion left;
	TextureRegion right;
	TextureRegion cubeControl;

	public OnScreenController() {
		loadAssets();
	}

	private void loadAssets () {
		Texture texture = new Texture(Gdx.files.internal("data/controls.png"));
		TextureRegion[] buttons = TextureRegion.split(texture, 64, 64)[0];
		left = buttons[0];
		right = buttons[1];
		cubeControl = buttons[3];
	}

	public void render (SpriteBatch batch) {
		//if (Gdx.app.getType() != ApplicationType.Android) return;
		batch.draw(left, 0, 0);
		batch.draw(right, 70, 0);
		batch.draw(cubeControl, TinyWorld.WIDTH - 64, 0);
	}

	public void dispose () {
		left.getTexture().dispose();
	}
}

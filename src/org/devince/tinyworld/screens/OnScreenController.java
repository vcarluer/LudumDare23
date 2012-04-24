
package org.devince.tinyworld.screens;

import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class OnScreenController {
	public static final int CONTROL_SIZE = 64;
	public static final int SCREEN_PADDING = 15;
	public static final int X_LEFT = SCREEN_PADDING + CONTROL_SIZE;
	public static final int PADDING = 26;
	public static final int PADDINGMID = PADDING / 2;
	public static final int X_RIGHT = X_LEFT + PADDING + CONTROL_SIZE  + PADDING;
	public static final int X_CREATE = TinyWorld.WIDTH - CONTROL_SIZE - SCREEN_PADDING;
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
		batch.draw(left, SCREEN_PADDING, SCREEN_PADDING);
		batch.draw(right, X_LEFT + PADDING, SCREEN_PADDING);
		batch.draw(cubeControl, X_CREATE, SCREEN_PADDING);
	}

	public void dispose () {
		left.getTexture().dispose();
	}
}

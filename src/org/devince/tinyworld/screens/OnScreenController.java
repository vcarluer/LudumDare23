
package org.devince.tinyworld.screens;

import org.devince.tinyworld.Assets;
import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.Application.ApplicationType;
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
	public static final int Y_POS = (int) (TinyWorld.HEIGHT - CONTROL_SIZE /2f - SCREEN_PADDING);
	public static final int Y_BOTTOM = (int) (Y_POS - CONTROL_SIZE /2f);
	public static final int X_POS = (int) (X_CREATE + CONTROL_SIZE / 2f);
	TextureRegion left;
	TextureRegion right;
	TextureRegion bridgeControl;
	TextureRegion helpControl;
	TextureRegion refreshControl;

	public OnScreenController() {
		loadAssets();
	}

	private void loadAssets () {
		Texture texture = Assets.getTexture("data/hud.png");
		TextureRegion[][] buttons = TextureRegion.split(texture, 8,8);
		left = buttons[0][3];
		right = buttons[0][3];
		bridgeControl = buttons[0][2];
		helpControl = buttons[0][1];
		refreshControl = buttons[0][0];
	}

	public void render (SpriteBatch batch) {
		//if (Gdx.app.getType() != ApplicationType.Android) return;
		batch.draw(left, SCREEN_PADDING + (4 * 8), SCREEN_PADDING + (4 * 8), 4, 4, 8, 8, 8, 8, 90f, false);
		batch.draw(right, X_LEFT + PADDING + (4 * 8), SCREEN_PADDING + (4 * 8), 4, 4, 8, 8, 8, 8, -90f, false);
		batch.draw(bridgeControl, X_POS, SCREEN_PADDING + (4 * 8), 4, 4, 8, 8, 8, 8, 0f, false);
		batch.draw(refreshControl,SCREEN_PADDING + (4 * 8), Y_POS, 4, 4, 8, 8, 8, 8, -90f, false);
		batch.draw(helpControl, X_POS, Y_POS, 4, 4, 8, 8, 8, 8, -90f, false);
	}

	public void dispose () {
		left.getTexture().dispose();
	}
}

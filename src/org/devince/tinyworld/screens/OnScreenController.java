
package org.devince.tinyworld.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class OnScreenController {
	public static final int SCREEN_PADDING = 15;
	
	public Button left;
	public Button right;
	public Button bridgeControl;
	public Button pauseControl;
	public Button helpControl;
	public Button refreshControl;	

	public OnScreenController() {
		loadAssets();
	}

	private void loadAssets () {
		String png = "data/hud.png";		
		this.left = new Button(true, png, 0, 1, SCREEN_PADDING, SCREEN_PADDING);
		this.right = new Button(true, png, 0, 0, left.getRightX() + SCREEN_PADDING, SCREEN_PADDING);
		this.bridgeControl = new Button(true, png, 0, 2, Gdx.graphics.getWidth() - Button.CONTROL_SIZE - SCREEN_PADDING, SCREEN_PADDING);
		float topY = Gdx.graphics.getHeight() - Button.CONTROL_SIZE - SCREEN_PADDING;
		this.pauseControl = new Button(true, png, 0, 3, SCREEN_PADDING, topY);		
		this.helpControl = new Button(true, png, 0, 4, this.pauseControl.getRightX() + SCREEN_PADDING, topY);
		this.refreshControl = new Button(true, png, 0, 5, Gdx.graphics.getWidth() - Button.CONTROL_SIZE - SCREEN_PADDING, topY);
	}

	public void render (SpriteBatch batch) {
		this.left.render(batch);
		this.right.render(batch);
		this.bridgeControl.render(batch);
		this.pauseControl.render(batch);
		this.helpControl.render(batch);
		this.refreshControl.render(batch);
	}

	public void dispose () {
		left.dispose();
		right.dispose();
		bridgeControl.dispose();
		pauseControl.dispose();
		helpControl.dispose();
		refreshControl.dispose();		
	}
}

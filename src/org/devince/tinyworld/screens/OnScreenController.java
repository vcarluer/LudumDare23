
package org.devince.tinyworld.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class OnScreenController {
	private static float baseDensity = 1.5f;
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
		this.left = new Button(true, png, 0, 1, getPadding(), getPadding());
		this.right = new Button(true, png, 0, 0, left.getRightX() + getPadding(), getPadding());
		this.bridgeControl = new Button(true, png, 0, 2, Gdx.graphics.getWidth() - Button.GetControlSize() - getPadding(), getPadding());
		float topY = Gdx.graphics.getHeight() - Button.GetControlSize() - getPadding();
		this.pauseControl = new Button(true, png, 0, 3, getPadding(), topY, true);		
		// this.helpControl = new Button(true, png, 0, 4, this.pauseControl.getRightX() + SCREEN_PADDING, topY);
		this.helpControl = new Button(true, png, 0, 4, getPadding(), topY - Button.GetControlSize() - getPadding(), true);
		this.refreshControl = new Button(true, png, 0, 5, Gdx.graphics.getWidth() - Button.GetControlSize() - getPadding(), topY, true);
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
	
	public void initGame() {
		this.left.setVisible(true);
		this.right.setVisible(true);
		this.bridgeControl.setVisible(true);
		this.pauseControl.setVisible(true);
		this.helpControl.setVisible(false);
		this.refreshControl.setVisible(false);
	}
	
	public void pauseGame() {
		this.left.setVisible(false);
		this.right.setVisible(false);
		this.bridgeControl.setVisible(false);
		this.pauseControl.setVisible(true);
		this.helpControl.setVisible(true);
		this.refreshControl.setVisible(true);
	}
	
	public static float getDensity() {
		return Gdx.graphics.getDensity() / baseDensity;
	}
	
	private static float getPadding() {
		return SCREEN_PADDING * getDensity();
	}
}

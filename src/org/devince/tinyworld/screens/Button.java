package org.devince.tinyworld.screens;

import org.devince.tinyworld.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Button {
	private static final int CONTROL_SIZE = 81; // on a 800*600 screen
	private static final int BASE_SIZE = 8;	
	
	private TextureRegion textureControl;
	private boolean onlyAndroid;
	private Texture textureBack;
	
	private float x;
	private float y;
	private float width;
	private float height;
	private boolean visible;
	private boolean skipBackground;
	
	public Button(boolean onlyAndroid, String pngFile, int pngX, int pngY, float posX, float posY, boolean skipBackground) {
		this(onlyAndroid, pngFile, pngX, pngY, posX, posY);
		this.skipBackground = skipBackground;
	}
	
	public Button(boolean onlyAndroid, String pngFile, int pngX, int pngY, float posX, float posY)	{
		this.onlyAndroid = onlyAndroid;
		this.textureControl = this.loadAssets(pngFile, pngX, pngY);
		this.x = posX;
		this.y = posY;
		this.width = GetControlSize();
		this.height = GetControlSize();
		this.visible = true;
	}
	
	private TextureRegion loadAssets (String pngFile, int x, int y) {
		this.textureBack = Assets.getTexture(Assets.DATA_BACK_BUTTON);
		Texture texture = Assets.getTexture(pngFile);
		TextureRegion[][] buttons = TextureRegion.split(texture, BASE_SIZE, BASE_SIZE);
		TextureRegion textureRegion = buttons[x][y];
		return textureRegion;
	}
	
	public void render (SpriteBatch batch) {
		if (!this.visible) return;
		if (this.onlyAndroid && Gdx.app.getType() != ApplicationType.Android) return;
		
		if (this.textureControl != null) {
			if (this.textureBack != null && ! this.skipBackground) {
				batch.draw(this.textureBack, this.x, this.y, this.width, this.height);
			}
			
			batch.draw(this.textureControl, this.x, this.y, this.width, this.height);
		}		
	}
	
	public void dispose () {
		if (this.textureControl != null) {
			this.textureControl.getTexture().dispose();
		}		
		
		if (this.textureBack != null) {
			this.textureBack.dispose();
		}
	}
	
	public boolean isTouched(float touchX, float touchY) {
		if (!this.visible) return false;
		
		return touchX > this.x && touchX < this.x + this.width && touchY > this.y && touchY < this.y + this.height;
	}
	
	public float getRightX() {
		return this.x + this.width;
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;

	}
	
	public static float GetControlSize() {
		return CONTROL_SIZE * OnScreenController.getDensity();
	}
}

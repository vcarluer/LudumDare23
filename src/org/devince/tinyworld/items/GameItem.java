package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class GameItem extends Actor {
	protected Sprite sprite;
	protected Point galaxyPoint;
	protected Rectangle boundingBox;
	
	public GameItem() {
		this.width = this.getRefereceWidth();
		this.height = this.getReferenceHeight();
		this.galaxyPoint = new Point();
		this.boundingBox = new Rectangle();
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setProjectionMatrix(TinyWorld.get().getCamera().combined);
		
		if (this.sprite != null) {
			this.sprite.setPosition(this.x - this.getRefereceWidth() / 2f, this.y - this.getReferenceHeight() / 2f);
			this.sprite.setRotation(this.rotation);
			this.sprite.setScale(this.scaleX, this.scaleY);
			this.sprite.draw(batch);
		}
	}
	
	protected void setSprite(String path) {
		this.sprite = new Sprite(new Texture(Gdx.files.internal(path)));
	}
	
	protected abstract float getRefereceWidth();
	protected abstract float getReferenceHeight();
	
	public Point getGalaxyPoint() {
		return this.galaxyPoint;
	}
	
	public Rectangle getBoundingBox() {
		return this.getBoundingBox(this.x, this.y);
	}
	
	public Rectangle getBoundingBox(float x, float y) {
		this.boundingBox.x = x - this.width / 2f;
		this.boundingBox.y = y - this.height / 2f;
		this.boundingBox.width = this.width;
		this.boundingBox.height = this.height;
		return this.boundingBox;
	}
	
	public Rectangle createBoundingBox(float x, float y) {
		return new Rectangle(x - this.width / 2f, y - this.height / 2f, this.width, this.height);
	}
}

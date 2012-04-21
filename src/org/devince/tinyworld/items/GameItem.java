package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class GameItem extends Actor {
	protected Sprite sprite;
	protected Point galaxyPoint;
	
	public GameItem() {
		this.width = this.getRefereceWidth();
		this.height = this.getReferenceHeight();
		this.galaxyPoint = new Point();
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setProjectionMatrix(TinyWorld.get().getCamera().combined);
		
		if (this.sprite != null) {
			this.sprite.setPosition(this.x - this.getRefereceWidth() / 2f, this.y - this.getReferenceHeight() / 2f);	
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
}

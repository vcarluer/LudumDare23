package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class GameItem extends Actor {
	protected Sprite sprite;
	protected int worldX;
	protected int worldY;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + worldX;
		result = prime * result + worldY;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameItem other = (GameItem) obj;
		if (worldX != other.worldX)
			return false;
		if (worldY != other.worldY)
			return false;
		return true;
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setProjectionMatrix(TinyWorld.get().getCamera().combined);
		this.sprite.setPosition(this.x, this.y);
	}
}

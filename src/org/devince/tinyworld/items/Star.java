package org.devince.tinyworld.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Star extends GameItem {
	public Star(float x, float y, Texture texture) {
		this.sprite = new Sprite(texture);
		this.x = x;
		this.y = y;
	}
	@Override
	protected float getRefereceWidth() {
		return 2;
	}

	@Override
	protected float getReferenceHeight() {
		return 2;
	}

	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}

}

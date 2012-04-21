package org.devince.tinyworld.items;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends GameItem {
	
	public Player(float x, float y) {
		this.setSprite("data/player.png");
		this.x = x;
		this.y = y;
	}
	
	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected float getRefereceWidth() {
		return 8;
	}

	@Override
	protected float getReferenceHeight() {
		return 8;
	}

}

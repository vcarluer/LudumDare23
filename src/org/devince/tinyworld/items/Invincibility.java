package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Invincibility extends GameItem {

	public Invincibility() {
		this.setSprite("data/invincible.png");
	}
	
	@Override
	protected float getRefereceWidth() {
		return 4;
	}

	@Override
	protected float getReferenceHeight() {
		return 4;
	}

	@Override
	public void handleContact(GameItem item) {
		if (item == TinyWorld.get().getPlayer()) {
			TinyWorld.get().getPlayer().startInvincible();
			TinyWorld.get().addItemToRemove(this);
		}
	}

	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}

}

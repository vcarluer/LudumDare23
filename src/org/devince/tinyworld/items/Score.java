package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Score extends GameItem {
	
	private static final int SCORE = 1;

	public Score() {
		this.setSprite("data/score.png");
	}
	
	@Override
	protected float getRefereceWidth() {
		return 4f;
	}

	@Override
	protected float getReferenceHeight() {
		return 4f;
	}

	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleContact(GameItem item) {
		if (item == TinyWorld.get().getPlayer()) {
			TinyWorld.get().addScore(SCORE);
			this.destroy();
		}
	}	
}

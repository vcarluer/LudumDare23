package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Life extends GameItem {
	private Sound sndLife;
	public Life() {
		this.setSprite("data/life.png");
		this.sndLife = this.sndLoad("data/life.wav");
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
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleContact(GameItem item) {
		if (item == TinyWorld.get().getPlayer()) {
			TinyWorld.get().getPlayer().addLife();
			this.sndLife.play();
			this.destroy();
			TinyWorld.get().getGameScreen().takeLife();
		}
	}

}

package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Shoot extends GameItem {
	
	private static final float VELOCITY = 100f;
	// Normalized target
	private Vector2 norTarget;

	public Shoot(float x, float y, float dirX, float dirY) {
		this.x = x;
		this.y = y;
		this.setSprite("data/shoot.png");
		this.norTarget = new Vector2(dirX - x, dirY - y);
		this.norTarget.nor();
	}
	
	@Override
	protected float getRefereceWidth() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public void handleContact(GameItem item) {
		if (item instanceof IHurtable) {
			IHurtable h = (IHurtable) item;
			h.hurt(this);
			TinyWorld.get().addItemToRemove(this);
		}
	}

	@Override
	public void act(float delta) {
		float x = this.norTarget.x * delta * VELOCITY;
		float y = this.norTarget.y * delta * VELOCITY;
		this.x += x;
		this.y += y;
		super.act(delta);
	}

	@Override
	protected float getReferenceHeight() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}
}

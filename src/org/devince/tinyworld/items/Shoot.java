package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Shoot extends GameItem {
	public static float SIZE = 4f;
	private static final float MAX_VELOCITY = 100f;
	private static final float BASE_VELOCITY = 10f;
	// Normalized target
	private Vector2 norTarget;

	public Shoot(float x, float y, float dirX, float dirY) {
		this.x = x;
		this.y = y;
		this.setSprite(this.getSprite());
		this.norTarget = new Vector2(dirX - x, dirY - y);
		this.norTarget.nor();
		
		this.name = this.getRandomSurname() + " the Shooter";
	}
	
	protected String getSprite() {
		return "data/shoot.png";
	}

	@Override
	protected float getRefereceWidth() {
		return SIZE;
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
		if (!TinyWorld.get().getGamePort().overlaps(this.getBoundingBox())) {
			TinyWorld.get().addItemToRemove(this);
			return;
		}
		
		float x = this.norTarget.x * delta * this.getVelocity();
		float y = this.norTarget.y * delta * this.getVelocity();
		this.x += x;
		this.y += y;
		super.act(delta);
	}

	private float getVelocity() {
		float vel = TinyWorld.get().getLevel() * BASE_VELOCITY;
		if (vel > MAX_VELOCITY) {
			vel = MAX_VELOCITY;
		}
		
		return vel;
	}

	@Override
	protected float getReferenceHeight() {
		return SIZE;
	}

	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}
}

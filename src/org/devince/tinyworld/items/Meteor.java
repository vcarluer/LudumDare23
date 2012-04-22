package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

public class Meteor extends Shoot {

	private static final float METEOR_SIZE = 6f;
	private static final float METEOR_REFERENCE = 8f;
	private static final int LIFE = 3;
	private int life;

	public Meteor(float x, float y, float dirX, float dirY) {
		super(x, y, dirX, dirY);
		this.width = METEOR_SIZE;
		this.height = METEOR_SIZE;
		
		this.life = LIFE;
	}
	
	@Override
	protected String getSprite() {
		return "data/meteor.png";
	}

	@Override
	protected float getRefereceWidth() {
		return METEOR_REFERENCE;
	}

	@Override
	protected float getReferenceHeight() {
		return METEOR_REFERENCE;
	}
	
	@Override
	public void handleContact(GameItem item) {
		if (item instanceof IHurtable) {
			IHurtable h = (IHurtable) item;
			h.hurt(this);
			h.hurt(this);
			this.life--;
			if (this.life <= 0) {
				TinyWorld.get().addItemToRemove(this);
			}
		}
	}
}
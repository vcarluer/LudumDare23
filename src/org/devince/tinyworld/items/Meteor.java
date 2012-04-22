package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.scenes.scene2d.actions.Forever;
import com.badlogic.gdx.scenes.scene2d.actions.RotateBy;

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
		if (TinyWorld.get().isTier1()) {
			this.life++;
		}
		
		if (TinyWorld.get().isTier2()) {
			this.life++;
		}
		
		RotateBy rb = RotateBy.$(360, 5f);
		Forever forever = Forever.$(rb);
		this.action(forever);
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
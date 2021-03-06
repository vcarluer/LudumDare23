package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.scenes.scene2d.actions.Forever;
import com.badlogic.gdx.scenes.scene2d.actions.RotateBy;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;

public class Meteor extends Shoot {

	private static final float METEOR_SIZE = 6f;
	private static final float METEOR_REFERENCE = 8f;
	private static final int LIFE = 2;
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
		
		this.clearActions();
		
		RotateBy rb = RotateBy.$(360, 5f);
		Forever forever = Forever.$(rb);
		this.action(forever);
		
		this.scaleX = 10;
		this.scaleY = 10;
		ScaleTo st = ScaleTo.$(1, 1, 2.0f);
		this.action(st);
		
		this.name = "a beautiful Meteor";
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
				this.destroy();
			}
		}
	}
}
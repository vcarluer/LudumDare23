package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

public class ShootGenerator extends Shooter {
	private static final float SHOOT_DELTA = 6f;
	private static final float MIN_DELTA = 10f;
	private static final int MAX_METEOR = 6;
	private float shootDelta;
	private float shootElapsed;
	
	public ShootGenerator() {
		this(TinyWorld.get().getPlayer());
		this.ignoreBlock = true;
	}
	
	public ShootGenerator(GameItem target) {
		super(target);
		this.shootDelta = this.getShootDelta();
	}

	private float getShootDelta() {
		float d = (float) (1 + SHOOT_DELTA * Math.random()) * (10 / TinyWorld.get().getLevel());
		if (d < MIN_DELTA) {
			d = MIN_DELTA;
		}
		return d;
	}

	@Override
	public void act(float delta) {
		// Always place upper corner
		
		this.shootElapsed += delta;
		if (this.shootElapsed > this.shootDelta) {
			if (TinyWorld.get().getMeteorsCount() < MAX_METEOR) {
				this.shoot();
			}
			this.shootElapsed = 0f;
			this.shootDelta = this.getShootDelta();
		}
		
		super.act(delta);
	}
	
	@Override
	public void shoot() {
		double sel = Math.random();
		int randCorner = 0;
		if (sel > 0.5f) {
			randCorner = 1;
		}
		
		float x = TinyWorld.get().getViewPort().x + randCorner * TinyWorld.get().getViewPort().width;
		float y = TinyWorld.get().getViewPort().y + TinyWorld.get().getViewPort().height;
		this.setPosition(x, y);
		super.shoot();
	}

	@Override
	protected Shoot createShoot() {
		return new Meteor(this.x, this.y, this.target.x, this.target.y);
	}
}

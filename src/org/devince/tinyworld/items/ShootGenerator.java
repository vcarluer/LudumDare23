package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

public class ShootGenerator extends Shooter {
	private static final float SHOOT_DELTA = 2f;
	private float shootDelta;
	private float shootElapsed;
	
	public ShootGenerator() {
		this(TinyWorld.get().getPlayer());
		this.ignoreBlock = true;
	}
	
	public ShootGenerator(GameItem target) {
		super(target);
		this.shootDelta = SHOOT_DELTA;
	}

	@Override
	public void act(float delta) {
		// Always place upper corner
		
		float x = this.target.x - (TinyWorld.WIDTH / 2f * TinyWorld.get().getZoom());
		float y = this.target.y + (TinyWorld.HEIGHT / 2f * TinyWorld.get().getZoom());
		this.setPosition(x, y);
		
		this.shootElapsed += delta;
		if (this.shootElapsed > this.shootDelta) {
			this.shoot();
			this.shootElapsed = 0f;
		}
		
		super.act(delta);
	}
	
}

package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

public class ShootGenerator extends Shooter {
	private static final float SHOOT_DELTA = 2f;
	private float shootDelta;
	private float shootElapsed;
	
	public ShootGenerator() {
		this(TinyWorld.get().getPlayer());
	}
	
	public ShootGenerator(GameItem target) {
		super(target);
		this.shootDelta = SHOOT_DELTA;
	}

	@Override
	public void act(float delta) {
		// Always place upper corner
		this.x = this.target.x - (TinyWorld.WIDTH / 2f * TinyWorld.get().getZoom());
		this.y = this.target.y + (TinyWorld.HEIGHT / 2f * TinyWorld.get().getZoom());
		
		this.shootElapsed += delta;
		if (this.shootElapsed > this.shootDelta) {
			this.shoot();
			this.shootElapsed = 0f;
		}
		
		super.act(delta);
	}
	
}

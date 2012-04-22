package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

public class Alien extends Player {

	private static final int START_LIFE = 1;
	private static final float MAX_VELOCITY = 1f;
	private static final float BASE_VELOCITY = 0.2f;

	public Alien(float x, float y) {
		super(x, y);
		this.life = START_LIFE;
		if (TinyWorld.get().isTier1()) {
			this.life++;
		}
		
		if (TinyWorld.get().isTier2()) {
			this.life++;
		}
		
		double dir = Math.random();
		if (dir < 0.5f) {
			this.direction = RIGHT;
		} else {
			this.direction = LEFT;
		}
	}

	@Override
	public void act(float delta) {
		this.acceleration.x = this.direction * ACCELERATION_BASE;
		
		super.act(delta);
	}

	@Override
	protected String getSpritePath() {
		return "data/alien.png";
	}

	@Override
	public void handleContact(GameItem item) {
		if (item instanceof Player) {
			if (!(item instanceof Alien)) {
				Player p = (Player) item;
				p.hurt(this);
			}
		}
	}

	@Override
	protected float getMaxVelocity() {
		float vel = BASE_VELOCITY * (TinyWorld.get().getLevel() / 2f); // 10 levels for max vel
		if (vel > MAX_VELOCITY) {
			vel = MAX_VELOCITY;
		}
		
		return vel;
	}
}

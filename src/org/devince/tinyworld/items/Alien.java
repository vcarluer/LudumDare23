package org.devince.tinyworld.items;

public class Alien extends Player {

	private static final int START_LIFE = 1;
	private static final float MAX_VELOCITY = 0.5f;

	public Alien(float x, float y) {
		super(x, y);
		this.life = START_LIFE;
	}

	@Override
	public void act(float delta) {
		this.acceleration.x = ACCELERATION_BASE;
		super.act(delta);
	}

	@Override
	protected String getSpritePath() {
		return "data/alien.png";
	}

	@Override
	public void handleContact(GameItem item) {
		if (item instanceof Player) {
			Player p = (Player) item;
			p.hurt(this);
			this.hurt(p);
		}
		
		super.handleContact(item);
	}

	@Override
	protected float getMaxVelocity() {
		return MAX_VELOCITY;
	}
}

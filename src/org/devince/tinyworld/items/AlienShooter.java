package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.math.Vector2;

public class AlienShooter extends Alien {
	private static final float SHOOT_DELTA = 1f;
	private static final float MIN_DELTA = 0.5f;
	private Shooter shooter;
	private float shootDelta;
	private float shootElapsed;
	private Player player;
	private Vector2 dir;
	
	public AlienShooter(float x, float y) {
		super(x, y);
		this.player = TinyWorld.get().getPlayer();
		this.shooter = new Shooter(this.player);
		this.shootDelta = this.getShootDelta();
		this.dir = new Vector2();
	}
	
	@Override
	protected String getSpritePath() {
		return "data/shooter.png";
	}

	@Override
	public void act(float delta) {
		this.shootElapsed += delta;
		if (this.shootElapsed > this.shootDelta) {
			this.dir.x = this.player.x - this.x;
			this.dir.y = this.player.y - this.y;
			this.dir.nor().mul(this.width + Shoot.SIZE / 2f + 1);
			
			this.shooter.setPosition(this.x + this.dir.x, this.y + this.dir.y);
			this.shooter.shoot();
			this.shootElapsed = 0;
			this.shootDelta = this.getShootDelta();
		}
		super.act(delta);
	}
	
	private float getShootDelta() {
		float d = (float) (1 + SHOOT_DELTA * Math.random()) * (3 / TinyWorld.get().getLevel());
		if (d < MIN_DELTA) {
			d = MIN_DELTA;
		}
		return d;
	}

}

package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

public class AlienShooter extends Alien {
	private static final float SHOOT_DELTA = 1f;
	private static final float MIN_DELTA = 0.5f;
	private static final int MAX_SHOOT = 10;
	private static final float SHOOT_DISTANCE = 400f;
	private Shooter shooter;
	private float shootDelta;
	private float shootElapsed;
	private Player player;
	private Vector2 dir;
	
	private Sound sndShoot;
	
	public AlienShooter(float x, float y) {
		super(x, y);
		this.player = TinyWorld.get().getPlayer();
		this.shooter = new Shooter(this.player);
		this.shootDelta = this.getShootDelta();
		this.dir = new Vector2();
		this.sndShoot = this.sndLoad("data/enshoot.wav");
		
		this.name = this.getRandomSurname() + " the Shooter";
	}
	
	@Override
	protected String getSpritePath() {
		return "data/shooter.png";
	}

	@Override
	public void act(float delta) {
		this.shootElapsed += delta;
		if (this.shootElapsed > this.shootDelta) {
			if (TinyWorld.get().getShootsCount() < MAX_SHOOT) {
				if (this.getPosition().dst(TinyWorld.get().getPlayer().getPosition()) < SHOOT_DISTANCE) {
					this.dir.x = this.player.x - this.x;
					this.dir.y = this.player.y - this.y;
					this.dir.nor().mul(this.width + Shoot.SIZE / 2f + 1);
					
					this.shooter.setPosition(this.x + this.dir.x, this.y + this.dir.y);
					this.shooter.shoot();
					this.sndShoot.play();
				}
			}

			this.shootElapsed = 0;
			this.shootDelta = this.getShootDelta();
		}
		
		super.act(delta);
	}
	
	@Override
	protected float getMaxVelocity() {
		return super.getMaxVelocity() / 2f;
	}

	private float getShootDelta() {
		float d = (float) (1 + SHOOT_DELTA * Math.random()) * (10 / TinyWorld.get().getLevel());
		if (d < MIN_DELTA) {
			d = MIN_DELTA;
		}
		return d;
	}

}

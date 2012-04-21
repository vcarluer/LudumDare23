package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;
import org.devince.tinyworld.world.Galaxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends GameItem {
	
	private static final int RIGHT = 1;
	private static final int LEFT = -1;
	private static final int NONE = 0;
	private static final int Acceleration_Base = 20;
	private static final float DAMP = 0.9f;
	private static final float MAX_VELOCITY = 6f;
	private int direction;
	
	private Vector2 acceleration;
	private Vector2 velocity;
	private Vector2 normal;

	public Player(float x, float y) {
		this.setSprite("data/player.png");
		this.x = x;
		this.y = y;
		
		this.acceleration = new Vector2();
		this.velocity = new Vector2();
		this.normal = new Vector2(0, 1);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
		case Keys.RIGHT:
			this.direction = RIGHT;
			break;
		case Keys.LEFT:
			this.direction = LEFT;
			break;
		default:
			return false;
		}
		
		this.acceleration.x = this.direction * Acceleration_Base;
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		boolean cancelMove = false;
		switch(keycode) {
		case Keys.RIGHT:
			if (this.direction == RIGHT) {
				cancelMove = true;
			}
			
			break;
		case Keys.LEFT:
			if (this.direction == LEFT) {
				cancelMove = true;
			}
			
			break;
		default:
			return false;
		}
		
		if (cancelMove) {
			this.direction = NONE;
			this.acceleration.x = 0;
		}
		
		return true;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		if (this.acceleration.x != 0) {
			float acceTime = this.acceleration.x * delta;
			this.velocity.x += acceTime;
		} else {
			this.velocity.x *= DAMP; // not based on delta here...
		}
		
		if (this.velocity.x > MAX_VELOCITY) {
			this.velocity.x = MAX_VELOCITY;
		}
		
		if (this.velocity.x < - MAX_VELOCITY) {
			this.velocity.x = - MAX_VELOCITY;
		}
		
		Planet[] currentAround = TinyWorld.get().getGalaxy().getAroundPlanetsFromGamePosition(this.x, this.y);
		
		float nextX = this.x + this.getTX(this.velocity.x);
		float nextY = this.y + this.getTY(this.velocity.x);
		Planet[] nextAround = TinyWorld.get().getGalaxy().getAroundPlanetsFromGamePosition(nextX, nextY);
		
		// Case no bottom
		Planet bottom = this.getBottom(nextAround);
		if (bottom == null) {
			bottom = this.getBottom(currentAround);
			this.changeNormal();
			this.changeFace(bottom);
		} else {
			// case planet block
			Planet nextPlanet = null;
			boolean switchPlanet = false;
			if (this.velocity.x > 0) {
				nextPlanet = this.getRight(currentAround);
			}
			
			if (nextPlanet == null && this.velocity.x < 0) {
				nextPlanet = this.getLeft(currentAround);
			}
			
			if (nextPlanet != null) {
				Rectangle bb = new Rectangle(nextX - this.width / 2f, nextY - this.height / 2f, this.width, this.height);
				Rectangle pBB = nextPlanet.getBoundingBox();
				
				if (bb.overlaps(pBB)) {
					switchPlanet = true;
				}
			}
				
			if (switchPlanet) {
				this.changeNormalCC();
				this.changeFacePlain(nextPlanet);
			}
		}
		
		this.x += this.getTX(this.velocity.x);
		this.y += this.getTY(this.velocity.x);
		
	}
	
	public void changeFace(Planet planet) {
		if (this.velocity.x > 0) {
			this.x = this.getMiddleX(planet) - this.getTX(planet.width / 2f);
			this.y = this.getMiddleY(planet) - this.getTY(planet.height / 2f);
		}
		
		if (this.velocity.x < 0) {
			this.x = this.getMiddleX(planet) + this.getTX(planet.width / 2f);
			this.y = this.getMiddleY(planet) + this.getTY(planet.height / 2f);
		}
	}
	
	public void changeFacePlain(Planet planet) {
		if (this.velocity.x > 0) {
			this.x = this.getMiddleX(planet) - this.getTX(planet.width / 2f - this.width / 2f);
			this.y = this.getMiddleY(planet) - this.getTY(planet.height / 2f - this.width / 2f);
		}
		
		if (this.velocity.x < 0) {
			this.x = this.getMiddleX(planet) + this.getTX(planet.width / 2f - this.width / 2f);
			this.y = this.getMiddleY(planet) + this.getTY(planet.height / 2f - this.width / 2f);
		}
	}
	
	private float getMiddleX(Planet planet) {
		if (this.normal.y == 1) {
			return planet.x;
		}
		
		if (this.normal.x == 1) {
			return planet.x + planet.width / 2f + this.width / 2f;
		}
		
		if (this.normal.y == -1) {
			return planet.x;
		}
		
		if (this.normal.x == -1) {
			return planet.x - planet.width / 2f - this.width / 2f;
		}
		
		return 0;
	}
	
	private float getMiddleY(Planet planet) {
		if (this.normal.y == 1) {
			return planet.y + planet.height / 2f + this.height / 2f;
		}
		
		if (this.normal.x == 1) {
			return planet.y;
		}
		
		if (this.normal.y == -1) {
			return planet.y - planet.height / 2f - this.height / 2f;
		}
		
		if (this.normal.x == -1) {
			return planet.y;
		}
		
		return 0;
	}
	
	private void changeNormal(boolean cc) {
		float vel = 0;
		if (this.velocity.x > 0) {
			vel = 1;
		} else {
			vel = -1;
		}
		
		if (cc) {
			vel *= -1;
		}
		
		if (this.normal.y == 1) {
			this.normal.x = vel;
			this.normal.y = 0;
			return;
		}
		
		if (this.normal.x == 1) {
			this.normal.x =0;
			this.normal.y = -vel;
			return;
		}
		
		if (this.normal.y == -1) {
			this.normal.x = -vel;
			this.normal.y = 0;
			return;
		}
		
		if (this.normal.x == -1) {
			this.normal.x = 0;
			this.normal.y = vel;
			return;
		}
	}
	
	private void changeNormal() {
		this.changeNormal(false);
	}
	
	private void changeNormalCC() {
		this.changeNormal(true);
	}

	private Planet getBottom(Planet[] nextAround) {
		if (this.normal.y == 1) {
			return nextAround[Galaxy.BOTTOM];
		}
		
		if (this.normal.x == 1) {
			return nextAround[Galaxy.MIDDLE_LEFT];
		}
		
		if (this.normal.y == -1) {
			return nextAround[Galaxy.TOP];
		}
		
		if (this.normal.x == -1) {
			return nextAround[Galaxy.MIDDLE_RIGHT];
		}
		
		return null;
	}
	
	private Planet getRight(Planet[] nextAround) {
		if (this.normal.y == 1) {
			return nextAround[Galaxy.MIDDLE_RIGHT];
		}
		
		if (this.normal.x == 1) {
			return nextAround[Galaxy.BOTTOM];
		}
		
		if (this.normal.y == -1) {
			return nextAround[Galaxy.MIDDLE_LEFT];
		}
		
		if (this.normal.x == -1) {
			return nextAround[Galaxy.TOP];
		}
		
		return null;
	}
	
	private Planet getLeft(Planet[] nextAround) {
		if (this.normal.y == 1) {
			return nextAround[Galaxy.MIDDLE_LEFT];
		}
		
		if (this.normal.x == 1) {
			return nextAround[Galaxy.TOP];
		}
		
		if (this.normal.y == -1) {
			return nextAround[Galaxy.MIDDLE_RIGHT];
		}
		
		if (this.normal.x == -1) {
			return nextAround[Galaxy.BOTTOM];
		}
		
		return null;
	}

	private float getTX(float x) {
		if (this.normal.y == 1) {
			return x;
		}
		
		if (this.normal.x == 1) {
			return 0;
		}
		
		if (this.normal.y == -1) {
			return -x;
		}
		
		if (this.normal.x == -1) {
			return 0;
		}
		
		return x;
	}

	private float getTY(float y) {
		if (this.normal.y == 1) {
			return 0;
		}
		
		if (this.normal.x == 1) {
			return -y;
		}
		
		if (this.normal.y == -1) {
			return 0;
		}
		
		if (this.normal.x == -1) {
			return y;
		}
		
		return y;
	}
	
	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected float getRefereceWidth() {
		return 8;
	}

	@Override
	protected float getReferenceHeight() {
		return 8;
	}

}

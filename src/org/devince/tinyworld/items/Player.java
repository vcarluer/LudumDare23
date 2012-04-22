package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;
import org.devince.tinyworld.world.Galaxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends GameItem {
	private final static float EPSILON = 0.05f;
	private static final int RIGHT = 1;
	private static final int LEFT = -1;
	private static final int NONE = 0;
	protected static final int ACCELERATION_BASE = 5;
	private static final float DAMP = 0.9f;
	private static final float MAX_VELOCITY = 2f;
	private static final float SCALE_VELOCITY = 0.02f;
	private static final int UP = 1;
	private static final int DOWN = -1;
	private static final float MAX_SCALE = 1.1f;
	private static final float MIN_SCALE = 0.9f;
	private static final int START_LIFE = 3;
	private int direction;
	private float scale;
	
	protected Vector2 acceleration;
	private Vector2 velocity;
	private Vector2 normal;
	private boolean createBlock ;
	private int facing;
	private int scaleDirection;
	protected int life;

	public Player(float x, float y) {
		this.setSprite(this.getSpritePath());
		this.x = x;
		this.y = y;
		
		this.acceleration = new Vector2();
		this.velocity = new Vector2();
		this.normal = new Vector2(0, 1);
		this.scaleDirection = UP;
		this.scale = 1;
		
		this.life = START_LIFE;
	}
	
	protected String getSpritePath() {
		return "data/player.png";
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
		case Keys.SPACE:
			this.createBlock = true;
			break;
		default:
			return false;
		}
		
		this.acceleration.x = this.direction * ACCELERATION_BASE;
		
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
		
		Planet[] currentAround = TinyWorld.get().getGalaxy().getAroundPlanetsFromGamePosition(this.x, this.y);
		Planet bottom = this.getBottom(currentAround);
		
		if (bottom != null) {
			bottom.handleContact(this);
		}
		
		if (this.life <= 0) {
			TinyWorld.get().setGameOver();
		}
		
		// Create block action
		if (this.createBlock) {
			this.createBlock(currentAround);
		}
		
		// Move
		if (this.acceleration.x != 0) {
			float acceTime = this.acceleration.x * delta;
			this.velocity.x += acceTime;
		} else {
			this.velocity.x *= DAMP; // not based on delta here...
		}
		
		if (this.velocity.x > this.getMaxVelocity()) {
			this.velocity.x = this.getMaxVelocity();
		}
		
		if (this.velocity.x < - this.getMaxVelocity()) {
			this.velocity.x = - this.getMaxVelocity();
		}
		
		
		
		float nextX = this.x + this.getTX(this.velocity.x);
		float nextY = this.y + this.getTY(this.velocity.x);
		Planet[] nextAround = TinyWorld.get().getGalaxy().getAroundPlanetsFromGamePosition(nextX, nextY);
		
		// Case no bottom
		bottom = this.getBottom(nextAround);
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
				Rectangle bb = this.createBoundingBox(nextX, nextY);
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
		
		if (this.velocity.x > EPSILON || this.velocity.x < - EPSILON) {
			if (this.velocity.x > 0) {
				this.facing = RIGHT;
			} else {
				this.facing = LEFT;
			}
			
			this.scale += this.scaleDirection * SCALE_VELOCITY;
			
			if (this.scale > MAX_SCALE) {
				this.scale = MAX_SCALE;
				this.scaleDirection *= -1;
			}
			
			if (this.scale < MIN_SCALE) {
				this.scale = MIN_SCALE;
				this.scaleDirection *= -1;
			}
			
			if (this.normal.y == 1) {
				this.sprite.setOrigin(0.5f * this.width, 0);
			}
			
			if (this.normal.x == 1) {
				this.sprite.setOrigin(0f, 0.5f * this.height);
			}
			
			if (this.normal.y == -1) {
				this.sprite.setOrigin(0.5f * this.width, 1 * this.height);
			}
			
			if (this.normal.x == - 1) {
				this.sprite.setOrigin(1f * this.width, 0.5f * this.height);
			}
			
		} else {
			this.scale = 1;
		}
		
		this.scaleX = this.scale;
		this.scaleY = this.scale;
	}
	
	protected float getMaxVelocity() {
		return MAX_VELOCITY;
	}

	private void createBlock(Planet[] around) {
		float xPlanet = 0;
		float yPlanet = 0;
		boolean createPlanete = false;
		Vector2 vct = new Vector2();
		if (this.facing == RIGHT) {
			if (this.getBottomRight(around) == null) {
				vct.x = Galaxy.TILESIZE;
				vct.y = - Galaxy.TILESIZE;
				createPlanete = true;
			} else if (this.getRight(around) == null) {
				vct.x = Galaxy.TILESIZE;
				vct.y = 0;
				createPlanete = true;
			} else if (this.getTopRight(around) == null) {
				vct.x = Galaxy.TILESIZE;
				vct.y = Galaxy.TILESIZE;
				createPlanete = true;
			}
		}
		
		if (this.facing == LEFT) {
			if (this.getBottomLeft(around) == null) {
				vct.x = - Galaxy.TILESIZE;
				vct.y = - Galaxy.TILESIZE;
				createPlanete = true;
			} else if (this.getLeft(around) == null) {
				vct.x = - Galaxy.TILESIZE;
				vct.y = 0;
				createPlanete = true;
			} else if (this.getTopLeft(around) == null) {
				vct.x = - Galaxy.TILESIZE;
				vct.y = Galaxy.TILESIZE;
				createPlanete = true;
			}
		}
		
		if (createPlanete) {
			vct.rotate(- this.getNormalAngle());
			Point pt = TinyWorld.get().getGalaxy().getGalaxyCoordinate(this.x + vct.x, this.y + vct.y);
			
			TinyWorld.get().getGalaxy().addPlanet(pt.x, pt.y);
		}
		
		this.createBlock = false;
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
		
		this.scale = 1;
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
	
	private Planet getBottomRight(Planet[] nextAround) {
		if (this.normal.y == 1) {
			return nextAround[Galaxy.BOTTOM_RIGHT];
		}
		
		if (this.normal.x == 1) {
			return nextAround[Galaxy.BOTTOM_LEFT];
		}
		
		if (this.normal.y == -1) {
			return nextAround[Galaxy.TOP_LEFT];
		}
		
		if (this.normal.x == -1) {
			return nextAround[Galaxy.TOP_RIGHT];
		}
		
		return null;
	}
	
	private Planet getTopRight(Planet[] nextAround) {
		if (this.normal.y == 1) {
			return nextAround[Galaxy.TOP_RIGHT];
		}
		
		if (this.normal.x == 1) {
			return nextAround[Galaxy.BOTTOM_RIGHT];
		}
		
		if (this.normal.y == -1) {
			return nextAround[Galaxy.BOTTOM_LEFT];
		}
		
		if (this.normal.x == -1) {
			return nextAround[Galaxy.TOP_LEFT];
		}
		
		return null;
	}
	
	private Planet getBottomLeft(Planet[] nextAround) {
		if (this.normal.y == 1) {
			return nextAround[Galaxy.BOTTOM_LEFT];
		}
		
		if (this.normal.x == 1) {
			return nextAround[Galaxy.TOP_LEFT];
		}
		
		if (this.normal.y == -1) {
			return nextAround[Galaxy.TOP_RIGHT];
		}
		
		if (this.normal.x == -1) {
			return nextAround[Galaxy.BOTTOM_RIGHT];
		}
		
		return null;
	}
	
	private Planet getTopLeft(Planet[] nextAround) {
		if (this.normal.y == 1) {
			return nextAround[Galaxy.TOP_LEFT];
		}
		
		if (this.normal.x == 1) {
			return nextAround[Galaxy.TOP_RIGHT];
		}
		
		if (this.normal.y == -1) {
			return nextAround[Galaxy.BOTTOM_RIGHT];
		}
		
		if (this.normal.x == -1) {
			return nextAround[Galaxy.BOTTOM_LEFT];
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
	
	private float getNormalAngle() {
		if (this.normal.y == 1) {
			return 0;
		}
		
		if (this.normal.x == 1) {
			return 90;
		}
		
		if (this.normal.y == -1) {
			return 180;
		}
		
		if (this.normal.x == -1) {
			return 270;
		}
		
		return 0;
	}
	
	private float getAngleRadian() {
		return (float) Math.toRadians(this.getNormalAngle());
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

	public void hurt(GameItem from) {
		this.life--;
		if (from != null) {
			this.acceleration.x *= -1;
			this.velocity.x *= -1 * MAX_VELOCITY;
		}
	}

	@Override
	public Point getGalaxyPoint() {
		TinyWorld.get().getGalaxy().getGalaxyCoordinate(this.galaxyPoint, this.x, this.y);
		return super.getGalaxyPoint();
	}

}

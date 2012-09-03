package org.devince.tinyworld.items;

import org.devince.tinyworld.Assets;
import org.devince.tinyworld.TinyWorld;
import org.devince.tinyworld.world.Galaxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.Repeat;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;

public class Player extends GameItem implements IHurtable, InputProcessor {
	private final static float EPSILON = 0.01f;
	protected static final int RIGHT = 1;
	protected static final int LEFT = -1;
	private static final int NONE = 0;
	protected static final int ACCELERATION_BASE = 5;
	private static final float DAMP = 0.9f;
	private static final float MAX_VELOCITY = 1.1f;
//	private static final float SCALE_VELOCITY = 0.02f;
//	private static final int UP = 1;
//	private static final int DOWN = -1;
//	private static final float MAX_SCALE = 1.1f;
//	private static final float MIN_SCALE = 0.9f;
	private static final int START_LIFE = 3;
	private static final float INVINCIBLE_TIME = 5f;
	private static final int SCORE = 1;
	protected int direction;
//	private float scale;
	
	protected Vector2 acceleration;
	private Vector2 velocity;
	
	private boolean createBlock ;
	private int facing;
//	private int scaleDirection;
	protected int life;
	private boolean isInvincible;
	private float invincibleElapsed;
	private Texture invTexture;
	private boolean isPlayer;
	
	private Planet[] currentAround;
	private Planet[] nextAround;
	private Rectangle nextBB;
	
	private Vector2 tmpVect;
	
	// sounds
	private Sound sndCreate;
	private Sound sndHurt;	
	private Sound sndEndInvincible;
	private Sound sndInvincible;
	private GameItem killItem;
	
	public Player(float x, float y) {
		this.setSprite(this.getSpritePath());
		this.x = x;
		this.y = y;
		
		this.acceleration = new Vector2();
		this.velocity = new Vector2();
		
//		this.scaleDirection = UP;
//		this.scale = 1;
		
		this.life = START_LIFE;
		this.invTexture = Assets.getTexture("data/playerinv.png");
		this.sndCreate = this.sndLoad("data/createplan.wav");
		this.sndHurt = this.sndLoad("data/hurt.wav");
		this.sndEndInvincible = this.sndLoad(Assets.DATA_INVINCIBLEEND_WAV);
		this.sndInvincible = this.sndLoad(Assets.DATA_INVINCIBLEMUS_MP3);
		
		this.currentAround = TinyWorld.get().getGalaxy().createPlanetsStruc();
		this.nextAround = TinyWorld.get().getGalaxy().createPlanetsStruc();
		this.nextBB = new Rectangle();
		
		this.tmpVect = new Vector2();
	}
	
	protected String getSpritePath() {
		return "data/player.png";
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.SPACE) {
			this.createBlock = true;
			return true;
		}
		
		if (keycode == Keys.ESCAPE) {
			TinyWorld.get().restart();
			return true;
		}
		
		if (keycode == Keys.H) {
			TinyWorld.get().showHelp();
			return true;
		}
		
		if (keycode == Keys.P) {
			TinyWorld.get().togglePause();
			return true;
		}

		return false;
	}

	private boolean createHandled;
	private boolean invincibleActionDone;
	private boolean invincibleMusicStarted;
	
	private void handleKeys() {				
		if (this.isCreatePressed) {
			if (!this.createHandled) {
				this.createBlock = true;;
			}
		} else {
			if (this.createHandled) {
				this.createHandled = false;
			}
		}
		
		this.direction = NONE;
		this.acceleration.x = 0;
		if (Gdx.input.isKeyPressed(Keys.RIGHT) || this.rightPressed) {
			this.direction = RIGHT;
		}
		
		if (Gdx.input.isKeyPressed(Keys.LEFT) || this.leftPressed) {
			this.direction = LEFT;
		}
		
		this.acceleration.x = this.direction * ACCELERATION_BASE;
	}
	
	private boolean leftPressed;
	private boolean rightPressed;
	private boolean isCreatePressed;
	
	private boolean existsAndIsEnabled(int position) {
		return this.currentAround[position] != null && this.currentAround[position].enable;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (!this.getEnable()) return;
		
		if (this.isPlayer) {
			this.handleKeys();
		}
		
		TinyWorld.get().getGalaxy().getAroundPlanetsFromGamePosition(this.x, this.y, this.currentAround);
		Planet bottom = this.getBottom(currentAround); 
		
		// Create block action
		if (this.createBlock) {
			this.createBlock(currentAround);
		}
		
		if (this.isPlayer && bottom != null) {
			boolean full = false;
			if (this.existsAndIsEnabled(Galaxy.LEFT) && 
				this.existsAndIsEnabled(Galaxy.TOP) && 
				this.existsAndIsEnabled(Galaxy.RIGHT) && 
				this.existsAndIsEnabled(Galaxy.BOTTOM)) {
				full = true;
			}
			
			if (full) {
				boolean first = false;
				for(Planet around : this.currentAround) {				
					if (around != null && !around.isPrimary() && around != bottom) {
						if (!first) {
							around.playExploseSound();
							first = true;
						}
						
						around.handleDestroy();
					}
				}
			}
		}		
		
		if (this.isInvincible()) {
			this.invincibleElapsed += delta;
			if (!this.invincibleActionDone && this.invincibleElapsed > INVINCIBLE_TIME - 1f) {
				FadeOut fo = FadeOut.$(0.1f);
				FadeIn fi = FadeIn.$(0.1f);
				Sequence seq = Sequence.$(fo, fi);
				Repeat rep = Repeat.$(seq, 5);
				this.action(rep);
				this.invincibleActionDone = true;
			}
			if (this.invincibleElapsed > INVINCIBLE_TIME) {
				this.setInvincible(false);
				this.sprite.setTexture(this.baseTexture);
				this.sndEndInvincible.play();
				this.invincibleActionDone = false;
				this.invincibleMusicStarted = false;
				TinyWorld.get().resumeMusic();
			}
		}
		
		// kill Alien
		if (currentAround[Galaxy.CENTER] != null && this != TinyWorld.get().getPlayer()) {
			this.hurt(currentAround[Galaxy.CENTER]);
			if (this.life <= 0) {
				TinyWorld.get().addScore(SCORE);
				TinyWorld.get().getGameScreen().takeCoin();
			}
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
		TinyWorld.get().getGalaxy().getAroundPlanetsFromGamePosition(nextX, nextY, this.nextAround);
		
		Planet nextPlanet = null;
		if (this.isPlayer) {
			nextPlanet = currentAround[Galaxy.CENTER];
			
		}
		
		// handle sun here because no more contacts on planets
		if (bottom instanceof Sun) {
			bottom.handleContact(this);
		}
		
		if (nextPlanet != null) {
			this.changeNormalCC();
			this.changeFacePlain(nextPlanet);
		} else {
			// Case no bottom
			bottom = this.getBottom(nextAround);
			if (bottom == null) {
				bottom = this.getBottom(currentAround);
				if (bottom != null) {
					this.changeNormal();
					this.changeFace(bottom);
				} else {
					// Block has been destroyed under player...
					this.kill();
				}
			} else {
				// case planet block
				boolean switchPlanet = false;
				if (this.velocity.x > 0) {
					nextPlanet = this.getRight(currentAround);
				}
				
				if (nextPlanet == null && this.velocity.x < 0) {
					nextPlanet = this.getLeft(currentAround);
				}
				
				if (nextPlanet == null && this.isPlayer) {
					nextPlanet = currentAround[Galaxy.CENTER];
				}
				
				if (nextPlanet != null) {
					this.createBoundingBox(nextX, nextY, this.nextBB);
					Rectangle pBB = nextPlanet.getBoundingBox();
					
					if (this.nextBB.overlaps(pBB)) {
						switchPlanet = true;
					}
				}
					
				if (switchPlanet) {
					this.changeNormalCC();
					this.changeFacePlain(nextPlanet);
				}
			}
		}
		
		this.x += this.getTX(this.velocity.x);
		this.y += this.getTY(this.velocity.x);
		
		if (this.velocity.x > EPSILON || this.velocity.x < - EPSILON) {
			if (this.velocity.x > 0) {
				this.facing = RIGHT;
				if (this.isFlip) {
					this.sprite.flip(true, false);
					this.isFlip = false;
				}
			} else {
				this.facing = LEFT;
				if (!this.isFlip) {
					this.sprite.flip(true, false);
					this.isFlip = true;
				}
			}
				
//			this.scale += this.scaleDirection * SCALE_VELOCITY;
//			
//			if (this.scale > MAX_SCALE) {
//				this.scale = MAX_SCALE;
//				this.scaleDirection *= -1;
//			}
//			
//			if (this.scale < MIN_SCALE) {
//				this.scale = MIN_SCALE;
//				this.scaleDirection *= -1;
//			}
//			
//			if (this.normal.y == 1) {
//				this.sprite.setOrigin(0.5f * this.width, 0);
//			}
//			
//			if (this.normal.x == 1) {
//				this.sprite.setOrigin(0f, 0.5f * this.height);
//			}
//			
//			if (this.normal.y == -1) {
//				this.sprite.setOrigin(0.5f * this.width, 1 * this.height);
//			}
//			
//			if (this.normal.x == - 1) {
//				this.sprite.setOrigin(1f * this.width, 0.5f * this.height);
//			}
			
		} else {
//			this.scale = 1;
		}
		
//		this.scaleX = this.scale;
//		this.scaleY = this.scale;
		
		this.rotation = - this.getNormalAngle();
	}
	
	private boolean isFlip;
	
	private void kill() {
		this.life = 0;
		this.destroy();
	}

	protected float getMaxVelocity() {
		float max = MAX_VELOCITY;
		if (TinyWorld.get().isTier1()) {
			max += 0.1f;
		}
		
		if (TinyWorld.get().isTier2()) {
			max += 0.1f;
		}
		
		if (this.isInvincible) {
			max *= 1.33f;
		}
		
		return max;
	}	

	private void createBlock(Planet[] around) {
		boolean createPlanete = false;
		if (this.facing == RIGHT) {
			if (this.getBottomRight(around) == null) {
				this.tmpVect.x = Galaxy.TILESIZE;
				this.tmpVect.y = - Galaxy.TILESIZE;
				createPlanete = true;
			} else if (this.getRight(around) == null) {
				this.tmpVect.x = Galaxy.TILESIZE;
				this.tmpVect.y = 0;
				createPlanete = true;
			} else if (this.getTopRight(around) == null) {
				this.tmpVect.x = Galaxy.TILESIZE;
				this.tmpVect.y = Galaxy.TILESIZE;
				createPlanete = true;
			}
		}
		
		if (this.facing == LEFT) {
			if (this.getBottomLeft(around) == null) {
				this.tmpVect.x = - Galaxy.TILESIZE;
				this.tmpVect.y = - Galaxy.TILESIZE;
				createPlanete = true;
			} else if (this.getLeft(around) == null) {
				this.tmpVect.x = - Galaxy.TILESIZE;
				this.tmpVect.y = 0;
				createPlanete = true;
			} else if (this.getTopLeft(around) == null) {
				this.tmpVect.x = - Galaxy.TILESIZE;
				this.tmpVect.y = Galaxy.TILESIZE;
				createPlanete = true;
			}
		}
		
		if (createPlanete) {
			this.tmpVect.rotate(- this.getNormalAngle());
			Point pt = TinyWorld.get().getGalaxy().getGalaxyCoordinate(this.x + this.tmpVect.x, this.y + this.tmpVect.y);
			
			TinyWorld.get().getGalaxy().addPlanet(pt.x, pt.y);
			this.sndCreate.play();
		}
		
		this.createBlock = false;
		this.createHandled = true;
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
		
//		this.scale = 1;
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
		if (!this.isInvincible()) {
			this.life--;
			if (this.isPlayer) {
				this.sndHurt.play();
			}
			
			FadeOut fo = FadeOut.$(0.1f);
			FadeIn fi = FadeIn.$(0.1f);
			Sequence seq = Sequence.$(fo, fi);
			Repeat rep = Repeat.$(seq, 3);
			this.action(rep);
						
			this.velocity.x = -1 * this.facing * MAX_VELOCITY;
			this.direction = NONE;
			if (this.life <= 0) {
				this.killItem = from;
				this.destroy();
			}
		}
	}

	public int getLife() {
		return this.life;
	}
	
	@Override
	public void handleContact(GameItem item) {
		if (item instanceof Player) {
			Player p = (Player) item;
			p.hurt(this);
		}
		
		super.handleContact(item);
	}

	public void addLife() {
		this.life++;
	}

	public void startInvincible() {
		this.setInvincible(true);
		this.invincibleElapsed = 0f;
		this.sprite.setTexture(this.invTexture);
		TinyWorld.get().pauseMusic();
		this.sndInvincible.stop();
		this.sndInvincible.play();
		this.invincibleMusicStarted = true;
		this.invincibleActionDone = false;
	}

	public boolean isPlayer() {
		return isPlayer;
	}

	public void setPlayer(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}

	public boolean isInvincible() {
		return isInvincible;
	}

	public void setInvincible(boolean isInvincible) {
		this.isInvincible = isInvincible;
	}
	
	public GameItem getKillItem() {
		return this.killItem;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		float yy = Gdx.graphics.getHeight() - y;
		if (TinyWorld.get().getGameScreen().getController().left.isTouched(x, yy)) this.pressLeft(pointer);
		if (TinyWorld.get().getGameScreen().getController().right.isTouched(x, yy)) this.pressRight(pointer);
		this.isCreatePressed = TinyWorld.get().getGameScreen().getController().bridgeControl.isTouched(x, yy);
		
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		float yy = Gdx.graphics.getHeight() - y;
		boolean pauseButton = TinyWorld.get().getGameScreen().getController().pauseControl.isTouched(x, yy);
		if (pauseButton) {
			TinyWorld.get().togglePause();
		}
		
		boolean helpButton = TinyWorld.get().getGameScreen().getController().helpControl.isTouched(x, yy);
		if (helpButton) {
			TinyWorld.get().showHelp();
		}
		
		boolean refreshButton = TinyWorld.get().getGameScreen().getController().refreshControl.isTouched(x, yy);
		if (refreshButton) {
			TinyWorld.get().restart();
		}
		
		if (TinyWorld.get().getGameScreen().getController().left.isTouched(x, yy) && pointer == this.movePointer) {
			this.leftPressed = false;
		}
		
		if (TinyWorld.get().getGameScreen().getController().right.isTouched(x, yy) && pointer == this.movePointer) {
			this.rightPressed = false;
		}
		
		if (TinyWorld.get().getGameScreen().getController().bridgeControl.isTouched(x, yy)) {
			this.isCreatePressed = false;
		}
		
		return true;
	}
	
	private int movePointer;

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		float yy = Gdx.graphics.getHeight() - y;
		if (TinyWorld.get().getGameScreen().getController().left.isTouched(x, yy) && pointer == this.movePointer) {
			this.pressLeft(pointer);
		} else {
			if (pointer == this.movePointer) {
				this.leftPressed = false;
			}
		}
		
		if (TinyWorld.get().getGameScreen().getController().right.isTouched(x, yy) && pointer == this.movePointer) {
			this.pressRight(pointer);
		} else {
			if (pointer == this.movePointer) {
				this.rightPressed = false;
			}			
		}
		
		this.isCreatePressed = TinyWorld.get().getGameScreen().getController().bridgeControl.isTouched(x, yy);
		
		return true;
	}
	
	private void pressLeft(int pointer) {
		if (this.rightPressed) this.rightPressed = false;
		this.leftPressed = true;
		this.movePointer = pointer;
	}
	
	private void pressRight(int pointer) {
		if (this.leftPressed) this.leftPressed = false;
		this.rightPressed = true;
		this.movePointer = pointer;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}
}

package org.devince.tinyworld.items;

import org.devince.tinyworld.Assets;
import org.devince.tinyworld.TinyWorld;
import org.devince.tinyworld.world.Galaxy;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Planet extends GameItem implements IHurtable {
	
	private static final int MAX_LIFE = 3;
	protected boolean primary;
	protected int life;
	private Sprite backSprite;
	
	// sounds
	private Sound sndExtDestroy;
	private Sound sndPlanetExplose;
	private Sound sndPlanetHurt;

	public Planet(int x, int y, boolean primary) {
		if (!primary) {
			this.primary = this.getPrimary();
		} else {
			this.primary = primary;
		}
		
		this.setSprite(this.getSpritePath());
		
		this.galaxyPoint.setX(x);
		this.galaxyPoint.setY(y);
		
		this.x = this.galaxyPoint.getX() * Galaxy.TILESIZE;
		this.y = this.galaxyPoint.getY() * Galaxy.TILESIZE;
		
		this.life = this.getMaxLife();
		
		if (this.isPrimary()) {
			this.backSprite	 = new Sprite(Assets.getTexture(this.getBackSpritePath()));
		}
		
		int rot = (int) Math.floor(Math.random() * 4);
		switch(rot) {
		default:
		case 0:
			this.rotation = 0;
			break;
		case 1:
			this.rotation = 90f;
			break;
		case 2:
			this.rotation = 180f;
			break;
		case 3:
			this.rotation = 270f;
			break;
		}
		
		this.sndExtDestroy = this.sndLoad("data/extdestroy.wav");
		this.sndPlanetExplose = this.sndLoad("data/planetexplose.wav");
		this.sndPlanetHurt = this.sndLoad("data/planethurt.wav");
	}
	
	protected boolean getPrimary() {
		return false;
	}

	public String getBackSpritePath() {
		return "data/planetair.png";
	}

	protected int getMaxLife() {
		if (this.primary) {
			return MAX_LIFE * 2;
		} else {
			return MAX_LIFE;
		}
	}

	public Planet(int x, int y) {
		this(x, y, false);
	}
	
	protected String getSpritePath() {
		if (this.primary) {
			return "data/planet.png";
		} else {
			return "data/planetext.png";
		}
	}


	public Actor hit(float x, float y) {
		return null;
	}

	@Override
	protected float getRefereceWidth() {
		return 16;
	}


	@Override
	protected float getReferenceHeight() {
		return 16;
	}

	@Override
	public int getLife() {
		return this.life;
	}

	@Override
	public void hurt(GameItem from) {
		if (!(this instanceof Sun)) {
			this.life--;
			if (!this.primary) {
				if (this.life == 2) {
					this.sprite.setTexture(Assets.getTexture("data/planetext2.png"));
				}
				
				if (this.life == 1) {
					this.sprite.setTexture(Assets.getTexture("data/planetext3.png"));
				}
			} else {
				if (this.life == 4) {
					this.sprite.setTexture(Assets.getTexture("data/planet2.png"));
				}
				
				if (this.life == 2) {
					this.sprite.setTexture(Assets.getTexture("data/planet3.png"));
				}
			}
			
			
			if (this.life <= 0) {
				if (this.primary) {
					this.sndPlanetExplose.play();
				} else {
					this.sndExtDestroy.play();
				}
				TinyWorld.get().getGalaxy().removePlanet(this);
			} else {
				this.sndPlanetHurt.play();
			}
		}
	}

	public boolean isPrimary() {
		return this.primary;
	}
	
	public void drawBack(SpriteBatch batch) {
		if (this.backSprite != null) {
			this.backSprite.setPosition(this.x - this.getAirRefereceWidth() / 2f, this.y - this.getAirReferenceHeight() / 2f);
			this.backSprite.setRotation(this.rotation);
			this.backSprite.draw(batch);
		}
	}

	private float getAirReferenceHeight() {
		return 64f;
	}

	private float getAirRefereceWidth() {
		return 64f;
	}
}

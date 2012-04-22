package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;
import org.devince.tinyworld.world.Galaxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Planet extends GameItem implements IHurtable {
	
	private static final int MAX_LIFE = 3;
	protected boolean primary;
	protected int life;
	private Sprite backSprite;

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
		if (TinyWorld.get().isTier1()) {
			this.life--;
		}
		
		if (TinyWorld.get().isTier2()) {
			this.life--;
		}
		
		if (this.isPrimary()) {
			this.backSprite	 = new Sprite(new Texture(Gdx.files.internal(this.getBackSpritePath())));
		}
	}
	
	protected boolean getPrimary() {
		return false;
	}

	public String getBackSpritePath() {
		return "data/planetair.png";
	}

	protected int getMaxLife() {
		return MAX_LIFE;
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
		if (!this.primary) {
			this.life--;
			if (this.life <= 0) {
				TinyWorld.get().getGalaxy().removePlanet(this);
			}
		}
	}

	public boolean isPrimary() {
		return this.primary;
	}
	
	public void drawBack(SpriteBatch batch) {
		if (this.backSprite != null) {
			this.backSprite.setPosition(this.x - this.getAirRefereceWidth() / 2f, this.y - this.getAirReferenceHeight() / 2f);
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

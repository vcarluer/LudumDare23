package org.devince.tinyworld.items;

import org.devince.tinyworld.world.Galaxy;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Planet extends GameItem {
	
	protected boolean primary;

	public Planet(int x, int y, boolean primary) {
		this.primary = primary;
		
		this.setSprite(this.getSpritePath());
		
		this.galaxyPoint.setX(x);
		this.galaxyPoint.setY(y);
		
		this.x = this.galaxyPoint.getX() * Galaxy.TILESIZE;
		this.y = this.galaxyPoint.getY() * Galaxy.TILESIZE;
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
}

package org.devince.tinyworld.items;

import org.devince.tinyworld.world.Galaxy;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Planet extends GameItem {
	
	public Planet(int x, int y) {
		this.setSprite("data/planet.png");
		
		this.galaxyPoint.setX(x);
		this.galaxyPoint.setY(y);
		
		this.x = this.galaxyPoint.getX() * Galaxy.TILESIZE;
		this.y = this.galaxyPoint.getY() * Galaxy.TILESIZE;
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

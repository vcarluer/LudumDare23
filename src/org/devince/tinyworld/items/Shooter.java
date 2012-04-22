package org.devince.tinyworld.items;

import org.devince.tinyworld.TinyWorld;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Shooter extends GameItem {
	protected GameItem target;
	protected boolean ignoreBlock;
	
	public Shooter(GameItem target) {
		this.target = target;
	}
	
	@Override
	protected float getRefereceWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected float getReferenceHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}

	public void shoot() {
		if (this.ignoreBlock || !TinyWorld.get().getGalaxy().contains(this.getGalaxyPoint())) {
			Shoot shoot = this.createShoot();
			TinyWorld.get().addGameItem(shoot);
		}
	}
	
	protected Shoot createShoot() {
		return new Shoot(this.x, this.y, this.target.x, this.target.y);
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		TinyWorld.get().getGalaxy().getGalaxyCoordinate(this.galaxyPoint, this.x, this.y);
	}
}

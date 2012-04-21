package org.devince.tinyworld.items;

import org.devince.tinyworld.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Planet extends GameItem {
	
	public Planet(int x, int y) {
		this.setSprite("data/planet.png");
		
		this.worldX = x;
		this.worldY = y;
		
		this.x = this.worldX * World.TILESIZE;
		this.y = this.worldY * World.TILESIZE;
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

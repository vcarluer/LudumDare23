package org.devince.tinyworld.items;

import org.devince.tinyworld.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Planet extends GameItem {
	
	public Planet(int x, int y) {
		this.sprite = new Sprite(new Texture(Gdx.files.internal("data/planet.png")));
		
		this.worldX = x;
		this.worldY = y;
		
		this.x = this.worldX * World.TILESIZE;
		this.y = this.worldY * World.TILESIZE;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		this.sprite.draw(batch);
	}

	public Actor hit(float x, float y) {
		return null;
	}

}

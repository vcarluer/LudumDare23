package org.devince.tinyworld.world;

import org.devince.tinyworld.TinyWorld;
import org.devince.tinyworld.items.Alien;
import org.devince.tinyworld.items.GameItem;
import org.devince.tinyworld.items.Planet;
import org.devince.tinyworld.items.Player;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlanetGenerator extends GameItem {
	private static final int SPAWN_RADIUS = 5;
	private float spawnDelta;
	private float spawnTime;
	
	public PlanetGenerator() {
		this.spawnDelta = 2f;
	}
	
	@Override
	protected float getRefereceWidth() {
		return 0;
	}

	@Override
	protected float getReferenceHeight() {
		return 0;
	}

	@Override
	public Actor hit(float x, float y) {
		return null;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		this.spawnTime += delta;
		if (this.spawnTime > this.spawnDelta) {
			int x = - SPAWN_RADIUS + (int)(Math.random() * SPAWN_RADIUS); 
			int y = - SPAWN_RADIUS + (int)(Math.random() * SPAWN_RADIUS);
			Player p = TinyWorld.get().getPlayer();
			x += p.getGalaxyPoint().x;
			y += p.getGalaxyPoint().y;
			Planet planet = TinyWorld.get().getGalaxy().addPlanet(x, y);
			if (planet != null) {
				Alien alien = new Alien(0, 0);
				TinyWorld.get().addGameItemOnPlanet(alien, planet);
				this.spawnTime = 0;
			}
		}
	}

}

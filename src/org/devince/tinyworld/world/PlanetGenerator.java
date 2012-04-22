package org.devince.tinyworld.world;

import org.devince.tinyworld.TinyWorld;
import org.devince.tinyworld.items.Alien;
import org.devince.tinyworld.items.AlienShooter;
import org.devince.tinyworld.items.GameItem;
import org.devince.tinyworld.items.Invincibility;
import org.devince.tinyworld.items.Life;
import org.devince.tinyworld.items.Planet;
import org.devince.tinyworld.items.Player;
import org.devince.tinyworld.items.Score;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlanetGenerator extends GameItem {
	private static final int SPAWN_RADIUS = 5;
	private static final int MAX_ALIEN = 10;
	private static float SPAWN_MAX = 2f; 
	private static float SPAWN_BASE = 6f;
	private float spawnDelta;
	private float spawnTime;
	
	public PlanetGenerator() {
		this.spawnDelta = this.getSpawnDelta();
	}
	
	private float getSpawnDelta() {
		float delta = SPAWN_BASE - (TinyWorld.get().getLevel() / 3f);
		if (delta > SPAWN_MAX) {
			delta = SPAWN_MAX;
		}
		
		return delta;
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
			
			Planet[] planets = TinyWorld.get().getGalaxy().getAroundPlanets(x, y);
			Planet free = null;
			for(Planet planet : planets) {
				if (planet != null && planet.isPrimary()) {
					free = planet;
					break;
				}
			}
			
			Planet planet = null;
			if (free == null) {
				planet = TinyWorld.get().getGalaxy().addPlanet(x, y, true);
			} else {
				planet = free;
			}
			
			if (planet != null) {
				if (TinyWorld.get().getAlienCount() < TinyWorld.get().getLevel() * MAX_ALIEN) {
					double rand = Math.random();
					Alien alien = null;
					if (rand < 0.5f) {
						alien = new Alien(0, 0);
					} else {
						alien = new AlienShooter(0, 0);
					}
					
					TinyWorld.get().addGameItemOnPlanet(alien, planet);
				}
				
				this.randomItem(planet);
				
				this.spawnTime = 0;
				this.spawnDelta = this.getSpawnDelta();
			}
		}
	}

	private void randomItem(Planet planet) {
		int sel = (int) Math.floor(Math.random() * 10);
		GameItem item = null;
		if (sel < 7) {
			item = new Score();
		} else if (sel < 9) {
			item = new Life();
		} else {
			item = new Invincibility();
		}

		TinyWorld.get().addGameItemOnPlanet(item, planet);
	}

}

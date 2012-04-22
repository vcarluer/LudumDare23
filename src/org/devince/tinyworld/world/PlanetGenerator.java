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
import org.devince.tinyworld.items.Sun;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlanetGenerator extends GameItem {
	private static final int SPAWN_RADIUS = 5;
	private static final int MAX_ALIEN = 10;
	private static final int MAX_ITEMS = 20;
	private static float SPAWN_MAX = 2f; 
	private static float SPAWN_BASE = 12f;
	private static float SPAWN_MAX_ITEM = 2f; 
	private static float SPAWN_BASE_ITEM = 6f;
	
	private float spawnDelta;
	private float spawnTime;
	
	private float spawnDeltaItem;
	private float spawnTimeItem;
	
	public PlanetGenerator() {
		this.spawnDelta = SPAWN_MAX;
		this.spawnDeltaItem = SPAWN_MAX_ITEM;
	}
	
	private float getSpawnDelta() {
		float delta = SPAWN_BASE - (TinyWorld.get().getLevel());
		if (delta < SPAWN_MAX) {
			delta = SPAWN_MAX;
		}
		
		return delta;
	}
	
	private float getSpawnDeltaItem() {
		float delta = SPAWN_BASE_ITEM - (TinyWorld.get().getLevel());
		if (delta < SPAWN_MAX_ITEM) {
			delta = SPAWN_MAX_ITEM;
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
		this.spawnTimeItem += delta;
		Planet planet = null;
		Planet free = null;
		int x = (- SPAWN_RADIUS) + (int)(Math.random() * (SPAWN_RADIUS * 2)); 
		int y = (- SPAWN_RADIUS) + (int)(Math.random() * (SPAWN_RADIUS * 2));
		
		if (this.spawnTime > this.spawnDelta || this.spawnTimeItem > this.spawnDeltaItem) {
			Player p = TinyWorld.get().getPlayer();
			x += p.getGalaxyPoint().x;
			y += p.getGalaxyPoint().y;
			
			Planet[] planets = TinyWorld.get().getGalaxy().getAroundPlanets(x, y);
			for(Planet pla : planets) {
				if (pla != null && pla.isPrimary() && !(pla instanceof Sun)) {
					free = pla;
					break;
				}
			}
		}
		if (this.spawnTime > this.spawnDelta) {
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
				
				this.spawnTime = 0;
				this.spawnDelta = this.getSpawnDelta();
			}
		}
		
		if (this.spawnTimeItem > this.spawnDeltaItem) {
			if (planet == null && free != null) {
				planet = free;
			}
			
			if (planet != null) {
				if (TinyWorld.get().getItemsCount() < TinyWorld.get().getLevel() * MAX_ITEMS) {
					this.randomItem(planet);
				}
				
				this.spawnTimeItem = 0;
				this.spawnDeltaItem = this.getSpawnDeltaItem();
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
